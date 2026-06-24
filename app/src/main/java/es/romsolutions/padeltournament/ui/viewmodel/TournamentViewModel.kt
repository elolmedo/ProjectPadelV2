package es.romsolutions.padeltournament.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.data.model.Tournament
import es.romsolutions.padeltournament.data.model.Ranking
import es.romsolutions.padeltournament.data.model.Player
import es.romsolutions.padeltournament.data.repository.TournamentRepository
import es.romsolutions.padeltournament.data.repository.LeagueRepository
import es.romsolutions.padeltournament.data.repository.PlayerRepository
import es.romsolutions.padeltournament.data.model.TeamInput
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class TournamentViewModel(
    private val repository: TournamentRepository,
    private val leagueRepository: LeagueRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val TAG = "TournamentViewModel"

    private val adminIdFlow = MutableStateFlow<String?>(null)

    val allTournaments: StateFlow<List<Tournament>> = adminIdFlow
        .flatMapLatest { adminId ->
            repository.getTournamentsByAdmin(adminId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setAdminId(adminId: String?) {
        adminIdFlow.value = adminId
    }

    fun insertTournamentWithPlayers(tournament: Tournament, playerIds: List<Int>) = viewModelScope.launch {
        repository.insertTournamentWithPlayers(tournament, playerIds)
    }

    fun delete(tournament: Tournament) = viewModelScope.launch {
        repository.delete(tournament)
    }

    fun finishTournament(tournament: Tournament) = viewModelScope.launch {
        repository.update(tournament.copy(isFinished = true))
        calculateTournamentRankings(tournament.id)
    }

    fun startTournament(tournament: Tournament) = viewModelScope.launch {
        try {
            val playerIds = repository.getPlayersInTournament(tournament.id)
            val selectedPlayers = playerRepository.getPlayersByIds(playerIds)

            if (selectedPlayers.size < 4) return@launch

            val matches = when (tournament.type) {
                "AMERICANA" -> generateAmericanaRotation(tournament, selectedPlayers.map { it.id })
                "POZO" -> generatePozoMatches(tournament, selectedPlayers)
                else -> generateExpressMatches(tournament, selectedPlayers.map { it.id })
            }
            
            repository.startTournamentWithMatches(tournament.copy(isStarted = true), matches)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting tournament", e)
        }
    }

    private fun generatePozoMatches(tournament: Tournament, players: List<Player>): List<Match> {
        val sortedPlayers = players.shuffled()

        val playerIds = sortedPlayers.map { it.id }
        val numCourtsFromUser = tournament.numberCourts ?: (playerIds.size / 4)
        val maxPossibleCourts = playerIds.size / 4
        val actualCourts = Math.min(numCourtsFromUser, maxPossibleCourts)
        
        val playersOnCourtsCount = actualCourts * 4
        val activePlayers = playerIds.take(playersOnCourtsCount)
        val reserves = playerIds.drop(playersOnCourtsCount)
        
        val matches = mutableListOf<Match>()
        val calendar = Calendar.getInstance().apply { timeInMillis = tournament.dateTour ?: System.currentTimeMillis() }
        
        for (i in 0 until actualCourts) {
            val pIdx = i * 4
            matches.add(Match(
                tournamentId = tournament.id,
                player1Id = activePlayers[pIdx], player2Id = activePlayers[pIdx + 1],
                player3Id = activePlayers[pIdx + 2], player4Id = activePlayers[pIdx + 3],
                isByTime = tournament.scoreType == "TIME",
                playStart = calendar.timeInMillis,
                courtNumber = i + 1,
                roundNumber = 1
            ))
        }

        if (reserves.isNotEmpty()) {
            reserves.chunked(2).forEach { pair ->
                matches.add(Match(
                    tournamentId = tournament.id,
                    player1Id = pair[0], 
                    player2Id = if (pair.size > 1) pair[1] else -1,
                    player3Id = -1, player4Id = -1,
                    isByTime = tournament.scoreType == "TIME",
                    playStart = calendar.timeInMillis,
                    courtNumber = 0,
                    roundNumber = 1
                ))
            }
        }
        return matches
    }

    private suspend fun generatePozoMatchesForTeams(tournament: Tournament, teams: List<TeamInput>): List<Match> {
        val playerIds = teams.flatMap { listOf(it.player1Id, it.player2Id) }
        val players = playerRepository.getPlayersByIds(playerIds)
        val playerMap = players.associateBy { it.id }
        
        val teamsWithLevel = teams.map { team ->
            val level = (playerMap[team.player1Id]?.level ?: 3.0) + (playerMap[team.player2Id]?.level ?: 3.0)
            team to level
        }

        // Siempre barajamos los equipos aleatoriamente para el inicio del pozo
        val sortedTeams = teamsWithLevel.shuffled().map { it.first }

        val numCourtsFromUser = tournament.numberCourts ?: (teams.size / 2)
        val maxPossibleCourts = teams.size / 2
        val actualCourts = Math.min(numCourtsFromUser, maxPossibleCourts)
        
        val teamsOnCourtsCount = actualCourts * 2
        val activeTeams = sortedTeams.take(teamsOnCourtsCount)
        val reserveTeams = sortedTeams.drop(teamsOnCourtsCount)
        
        val matches = mutableListOf<Match>()
        val calendar = Calendar.getInstance().apply { timeInMillis = tournament.dateTour ?: System.currentTimeMillis() }
        
        for (i in 0 until actualCourts) {
            val tIdx = i * 2
            val t1 = activeTeams[tIdx]
            val t2 = activeTeams[tIdx + 1]
            matches.add(Match(
                tournamentId = tournament.id,
                player1Id = t1.player1Id, player2Id = t1.player2Id,
                player3Id = t2.player1Id, player4Id = t2.player2Id,
                isByTime = tournament.scoreType == "TIME",
                playStart = calendar.timeInMillis,
                courtNumber = i + 1,
                roundNumber = 1
            ))
        }

        if (reserveTeams.isNotEmpty()) {
            reserveTeams.forEach { team ->
                matches.add(Match(
                    tournamentId = tournament.id,
                    player1Id = team.player1Id, player2Id = team.player2Id,
                    player3Id = -1, player4Id = -1,
                    isByTime = tournament.scoreType == "TIME",
                    playStart = calendar.timeInMillis,
                    courtNumber = 0,
                    roundNumber = 1
                ))
            }
        }
        return matches
    }

    fun generateNextPozoRound(tournamentId: Int) = viewModelScope.launch {
        try {
            val tournament = repository.getTournamentById(tournamentId) ?: return@launch
            val allMatches = leagueRepository.getMatchesByTournamentSync(tournamentId)
            val lastRoundNumber = allMatches.maxOfOrNull { it.roundNumber } ?: 1
            val lastRoundMatches = allMatches.filter { it.roundNumber == lastRoundNumber }
            
            val numPlayers = tournament.numberPlayers
            val maxRounds = if (tournament.isTeamBased) (numPlayers / 2) - 1 else numPlayers - 1

            if (lastRoundNumber >= maxRounds) {
                finishTournament(tournament)
                return@launch
            }

            val courtResults = mutableListOf<PozoCourtResult>()
            lastRoundMatches.filter { it.courtNumber > 0 }.forEach { m ->
                val winnerPair = if (m.scoreTeamOne > m.scoreTeamTwo) Pair(m.player1Id, m.player2Id) else Pair(m.player3Id, m.player4Id)
                val loserPair = if (m.scoreTeamOne > m.scoreTeamTwo) Pair(m.player3Id, m.player4Id) else Pair(m.player1Id, m.player2Id)
                courtResults.add(PozoCourtResult(m.courtNumber, winnerPair, loserPair))
            }
            
            val currentReserves = mutableListOf<Int>()
            lastRoundMatches.filter { it.courtNumber == 0 }.forEach { m ->
                if (m.player1Id != -1) currentReserves.add(m.player1Id)
                if (m.player2Id != -1) currentReserves.add(m.player2Id)
            }

            courtResults.sortBy { it.courtId }
            val numCourts = courtResults.size
            val nextMatches = mutableListOf<Match>()
            val nextRoundNumber = lastRoundNumber + 1
            val duration = if (tournament.scoreType == "TIME") (tournament.matchDuration ?: 20) else 45
            val nextStartTime = (lastRoundMatches.firstOrNull()?.playStart ?: System.currentTimeMillis()) + (duration + 5) * 60 * 1000

            val nextReserves = mutableListOf<Int>()
            val courtPairings = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
            for(i in 1..numCourts) courtPairings[i] = mutableListOf()

            if (numCourts > 0) {
                // Pista 1
                courtPairings[1]?.add(courtResults[0].winners)
                if (numCourts > 1) {
                    courtPairings[2]?.add(courtResults[0].losers)
                } else {
                    nextReserves.addAll(listOf(courtResults[0].losers.first, courtResults[0].losers.second))
                }

                // Pistas Intermedias
                for (i in 1 until numCourts - 1) {
                    courtPairings[i]?.add(courtResults[i].winners)
                    courtPairings[i+2]?.add(courtResults[i].losers)
                }

                // Última Pista
                if (numCourts > 1) {
                    val lastIdx = numCourts
                    courtPairings[lastIdx - 1]?.add(courtResults[lastIdx - 1].winners)
                    nextReserves.addAll(listOf(courtResults[lastIdx - 1].losers.first, courtResults[lastIdx - 1].losers.second))
                }

                // Rotación de Reservas
                if (currentReserves.isNotEmpty()) {
                    val luckyWinnersFromReserve = currentReserves.take(2)
                    if (luckyWinnersFromReserve.size == 2) {
                        courtPairings[numCourts]?.add(Pair(luckyWinnersFromReserve[0], luckyWinnersFromReserve[1]))
                    }
                    if (currentReserves.size > 2) {
                        nextReserves.addAll(currentReserves.drop(2))
                    }
                }
            }

            for (i in 1..numCourts) {
                val pairs = courtPairings[i] ?: continue
                if (pairs.size == 2) {
                    nextMatches.add(Match(
                        tournamentId = tournamentId,
                        player1Id = pairs[0].first, player2Id = pairs[0].second,
                        player3Id = pairs[1].first, player4Id = pairs[1].second,
                        isByTime = tournament.scoreType == "TIME", playStart = nextStartTime, courtNumber = i, roundNumber = nextRoundNumber
                    ))
                }
            }

            if (nextReserves.isNotEmpty()) {
                nextReserves.chunked(2).forEach { pair ->
                    nextMatches.add(Match(
                        tournamentId = tournamentId,
                        player1Id = pair[0], player2Id = if(pair.size > 1) pair[1] else -1,
                        player3Id = -1, player4Id = -1,
                        isByTime = tournament.scoreType == "TIME", playStart = nextStartTime, courtNumber = 0, roundNumber = nextRoundNumber
                    ))
                }
            }

            repository.insertMatches(nextMatches)
        } catch (e: Exception) {
            Log.e(TAG, "Error generating next Pozo round", e)
        }
    }

    private data class PozoCourtResult(val courtId: Int, val winners: Pair<Int, Int>, val losers: Pair<Int, Int>)

    fun startTournamentWithTeams(tournament: Tournament, teams: List<TeamInput>) = viewModelScope.launch {
        try {
            if (tournament.type == "POZO") {
                val matches = generatePozoMatchesForTeams(tournament, teams)
                repository.startTournamentWithMatches(tournament.copy(isStarted = true), matches)
                return@launch
            }

            val rounds = generateRoundRobinRounds(teams)
            val matches = mutableListOf<Match>()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = tournament.dateTour ?: System.currentTimeMillis()
            val durationMins = if (tournament.matchDuration > 0) tournament.matchDuration else 75
            val numCourts = if (tournament.numberCourts > 0) tournament.numberCourts else 1
            rounds.forEachIndexed { rIdx, roundMatches ->
                var mInRIdx = 0
                while (mInRIdx < roundMatches.size) {
                    for (court in 1..numCourts) {
                        if (mInRIdx >= roundMatches.size) break
                        val pair = roundMatches[mInRIdx]
                        matches.add(Match(
                            tournamentId = tournament.id,
                            player1Id = pair.first.player1Id, player2Id = pair.first.player2Id,
                            player3Id = pair.second.player1Id, player4Id = pair.second.player2Id,
                            isByTime = tournament.scoreType == "TIME",
                            playStart = calendar.timeInMillis,
                            courtNumber = court,
                            weekNumber = 1,
                            roundNumber = rIdx + 1
                        ))
                        mInRIdx++
                    }
                    if (mInRIdx < roundMatches.size) {
                        calendar.add(Calendar.MINUTE, durationMins + 5)
                    }
                }
                if (rIdx < rounds.size - 1) {
                    calendar.add(Calendar.MINUTE, durationMins + 5)
                }
            }
            repository.startTournamentWithMatches(tournament.copy(isStarted = true), matches)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting tournament with teams", e)
        }
    }

    private fun generateRoundRobinRounds(teams: List<TeamInput>): List<List<Pair<TeamInput, TeamInput>>> {
        val list = teams.toMutableList()
        val numTeams = list.size
        val rotationSize = if (numTeams % 2 != 0) numTeams + 1 else numTeams
        val totalRounds = rotationSize - 1
        val matchesPerRound = rotationSize / 2
        val rounds = mutableListOf<List<Pair<TeamInput, TeamInput>>>()
        val indices = (0 until rotationSize).toMutableList()
        for (round in 0 until totalRounds) {
            val roundMatches = mutableListOf<Pair<TeamInput, TeamInput>>()
            for (i in 0 until matchesPerRound) {
                val idx1 = indices[i]
                val idx2 = indices[rotationSize - 1 - i]
                if (idx1 < numTeams && idx2 < numTeams) {
                    roundMatches.add(list[idx1] to list[idx2])
                }
            }
            rounds.add(roundMatches)
            val last = indices.removeAt(indices.size - 1)
            indices.add(1, last)
        }
        return rounds
    }

    private fun generateAmericanaRotation(tournament: Tournament, playerIds: List<Int>): List<Match> {
        val players = playerIds.shuffled()
        val n = players.size
        val allPairs = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                allPairs.add(players[i] to players[j])
            }
        }
        allPairs.shuffle()
        val usedPairs = mutableSetOf<Int>()
        val finalMatchups = mutableListOf<List<Int>>()
        for (i in allPairs.indices) {
            if (i in usedPairs) continue
            for (j in i + 1 until allPairs.size) {
                if (j in usedPairs) continue
                val p1 = allPairs[i]
                val p2 = allPairs[j]
                if (p1.first != p2.first && p1.first != p2.second && 
                    p1.second != p2.first && p1.second != p2.second) {
                    finalMatchups.add(listOf(p1.first, p1.second, p2.first, p2.second))
                    usedPairs.add(i)
                    usedPairs.add(j)
                    break
                }
            }
        }
        return scheduleMatches(tournament, finalMatchups)
    }

    private fun generateExpressMatches(tournament: Tournament, playerIds: List<Int>): List<Match> {
        val players = playerIds.shuffled()
        val numTeams = players.size / 2
        val teams = (0 until numTeams).map { i -> Pair(players[i * 2], players[i * 2 + 1]) }
        val matchups = mutableListOf<List<Int>>()
        if (numTeams >= 8) {
            val groupA = teams.take(numTeams / 2)
            val groupB = teams.drop(numTeams / 2)
            matchups.addAll(getRoundRobinMatchups(groupA))
            matchups.addAll(getRoundRobinMatchups(groupB))
        } else {
            for (i in 0 until numTeams / 2) {
                val t1 = teams[i * 2]
                val t2 = teams[i * 2 + 1]
                matchups.add(listOf(t1.first, t1.second, t2.first, t2.second))
            }
        }
        return scheduleMatches(tournament, matchups)
    }

    private fun getRoundRobinMatchups(teams: List<Pair<Int, Int>>): List<List<Int>> {
        val matchups = mutableListOf<List<Int>>()
        for (i in teams.indices) {
            for (j in i + 1 until teams.size) {
                matchups.add(listOf(teams[i].first, teams[i].second, teams[j].first, teams[j].second))
            }
        }
        return matchups
    }

    private fun scheduleMatches(tournament: Tournament, matchups: List<List<Int>>): List<Match> {
        val matches = mutableListOf<Match>()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = tournament.dateTour ?: System.currentTimeMillis()
        val durationMins = if (tournament.matchDuration > 0) tournament.matchDuration else 20
        val numCourts = if (tournament.numberCourts > 0) tournament.numberCourts else 1
        val bufferMins = 5
        var currentDurationToday = 0
        var matchIndex = 0
        while (matchIndex < matchups.size) {
            for (court in 1..numCourts) {
                if (matchIndex >= matchups.size) break
                val p = matchups[matchIndex]
                matches.add(Match(
                    tournamentId = tournament.id,
                    player1Id = p[0], player2Id = p[1],
                    player3Id = p[2], player4Id = p[3],
                    isByTime = tournament.scoreType == "TIME",
                    playStart = calendar.timeInMillis,
                    courtNumber = court
                ))
                matchIndex++
            }
            calendar.add(Calendar.MINUTE, durationMins + bufferMins)
            currentDurationToday += (durationMins + bufferMins)
            if (currentDurationToday >= tournament.maxHoursPerDay * 60) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val startCal = Calendar.getInstance().apply { timeInMillis = tournament.dateTour ?: System.currentTimeMillis() }
                calendar.set(Calendar.HOUR_OF_DAY, startCal.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, startCal.get(Calendar.MINUTE))
                currentDurationToday = 0
            }
        }
        return matches
    }

    private suspend fun calculateTournamentRankings(tournamentId: Int) {
        val tournamentMatches = leagueRepository.getMatchesByTournamentSync(tournamentId).filter { it.playFinish != null }
        val playerStats = mutableMapOf<Int, Ranking>()
        tournamentMatches.forEach { m ->
            val pIds = listOf(m.player1Id, m.player2Id, m.player3Id, m.player4Id)
            pIds.forEach { pid -> if (!playerStats.containsKey(pid)) playerStats[pid] = Ranking(leagueId = -tournamentId, playerId = pid) }
            val s1 = playerStats[m.player1Id]!!; val s2 = playerStats[m.player2Id]!!; val s3 = playerStats[m.player3Id]!!; val s4 = playerStats[m.player4Id]!!
            playerStats[m.player1Id] = s1.copy(matchesPlayed = s1.matchesPlayed + 1)
            playerStats[m.player2Id] = s2.copy(matchesPlayed = s2.matchesPlayed + 1)
            playerStats[m.player3Id] = s3.copy(matchesPlayed = s3.matchesPlayed + 1)
            playerStats[m.player4Id] = s4.copy(matchesPlayed = s4.matchesPlayed + 1)
            if (m.scoreTeamOne > m.scoreTeamTwo) {
                playerStats[m.player1Id] = playerStats[m.player1Id]!!.let { it.copy(matchesWon = it.matchesWon + 1, points = it.points + 3) }
                playerStats[m.player2Id] = playerStats[m.player2Id]!!.let { it.copy(matchesWon = it.matchesWon + 1, points = it.points + 3) }
                playerStats[m.player3Id] = playerStats[m.player3Id]!!.let { it.copy(matchesLost = it.matchesLost + 1) }
                playerStats[m.player4Id] = playerStats[m.player4Id]!!.let { it.copy(matchesLost = it.matchesLost + 1) }
            } else if (m.scoreTeamTwo > m.scoreTeamOne) {
                playerStats[m.player3Id] = playerStats[m.player3Id]!!.let { it.copy(matchesWon = it.matchesWon + 1, points = it.points + 3) }
                playerStats[m.player4Id] = playerStats[m.player4Id]!!.let { it.copy(matchesWon = it.matchesWon + 1, points = it.points + 3) }
                playerStats[m.player1Id] = playerStats[m.player1Id]!!.let { it.copy(matchesLost = it.matchesLost + 1) }
                playerStats[m.player2Id] = playerStats[m.player2Id]!!.let { it.copy(matchesLost = it.matchesLost + 1) }
            }
        }
        leagueRepository.updateRankings(playerStats.values.toList())
    }

    suspend fun getPlayersInTournament(tournamentId: Int): List<Int> {
        return repository.getPlayersInTournament(tournamentId)
    }
}

class TournamentViewModelFactory(
    private val repository: TournamentRepository,
    private val leagueRepository: LeagueRepository,
    private val playerRepository: PlayerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TournamentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TournamentViewModel(repository, leagueRepository, playerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
