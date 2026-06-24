package es.romsolutions.padeltournament.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.romsolutions.padeltournament.data.model.League
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.data.model.Ranking
import es.romsolutions.padeltournament.data.repository.LeagueRepository
import es.romsolutions.padeltournament.data.model.TeamInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class LeagueViewModel(private val repository: LeagueRepository) : ViewModel() {
    private val TAG = "LeagueViewModel"

    private val adminIdFlow = MutableStateFlow<String?>(null)

    val allLeagues: StateFlow<List<League>> = adminIdFlow
        .flatMapLatest { adminId ->
            repository.getLeaguesByAdmin(adminId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setAdminId(adminId: String?) {
        adminIdFlow.value = adminId
    }

    val allMatches: StateFlow<List<Match>> = repository.allMatches
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allTeams: StateFlow<List<es.romsolutions.padeltournament.data.model.Team>> = repository.allTeams
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val generalRanking: StateFlow<List<Ranking>> = repository.generalRanking
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getRankingForLeague(leagueId: Int): Flow<List<Ranking>> {
        return repository.getRankingForLeague(leagueId)
    }

    fun insertLeagueWithPlayers(league: League, playerIds: List<Int>) = viewModelScope.launch {
        repository.insertLeagueWithPlayersAndRanking(league, playerIds)
    }

    fun updateMatch(match: Match) = viewModelScope.launch {
        repository.updateMatch(match)
    }

    fun delete(league: League) = viewModelScope.launch {
        repository.delete(league)
    }
    
    suspend fun getPlayersInLeague(leagueId: Int): List<Int> {
        return repository.getPlayersInLeague(leagueId)
    }

    fun finishLeague(league: League) = viewModelScope.launch {
        repository.update(league.copy(isFinished = true))
        calculateRankings(league.id)
    }

    fun startLeagueWithTeams(
        league: League, 
        matchDays: List<Int>, 
        teams: List<TeamInput>,
        numCourts: Int,
        startHour: Int,
        startMinute: Int
    ) = viewModelScope.launch {
        try {
            val dbTeams = teams.map { input ->
                es.romsolutions.padeltournament.data.model.Team(
                    leagueId = league.id,
                    nameTeam = input.name,
                    playerOneId = input.player1Id,
                    playerTwoId = input.player2Id
                )
            }
            
            val rounds = generateRoundRobinRounds(teams)
            val matches = mutableListOf<Match>()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = league.startDate
            
            var roundIndex = 0
            var weekCounter = 1
            
            while (roundIndex < rounds.size) {
                var roundsScheduledThisWeek = 0
                for (d in 0..6) {
                    if (roundIndex >= rounds.size || roundsScheduledThisWeek >= league.weeklyMatches) break
                    
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    val adjustedDay = if (dayOfWeek == Calendar.SUNDAY) 7 else dayOfWeek - 1
                    
                    if (matchDays.contains(adjustedDay)) {
                        val currentRoundMatches = rounds[roundIndex]
                        val dayCal = calendar.clone() as Calendar
                        dayCal.set(Calendar.HOUR_OF_DAY, startHour)
                        dayCal.set(Calendar.MINUTE, startMinute)
                        
                        var matchInRoundIdx = 0
                        while (matchInRoundIdx < currentRoundMatches.size) {
                            for (court in 1..numCourts) {
                                if (matchInRoundIdx >= currentRoundMatches.size) break
                                val pair = currentRoundMatches[matchInRoundIdx]
                                matches.add(Match(
                                    leagueId = league.id,
                                    player1Id = pair.first.player1Id, player2Id = pair.first.player2Id,
                                    player3Id = pair.second.player1Id, player4Id = pair.second.player2Id,
                                    isByTime = true,
                                    playStart = dayCal.timeInMillis,
                                    courtNumber = court,
                                    weekNumber = weekCounter,
                                    roundNumber = roundIndex + 1
                                ))
                                matchInRoundIdx++
                            }
                            if (matchInRoundIdx < currentRoundMatches.size) {
                                dayCal.add(Calendar.MINUTE, 75)
                            }
                        }
                        roundIndex++
                        roundsScheduledThisWeek++
                    }
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                weekCounter++
            }
            
            val updatedLeague = league.copy(
                isStarted = true,
                matchDays = matchDays.joinToString(","),
                endDate = matches.lastOrNull()?.playStart ?: league.endDate,
                numberCourts = numCourts
            )
            repository.startLeagueWithMatchesAndTeams(updatedLeague, matches, dbTeams)
        } catch (e: Exception) {
            Log.e(TAG, "Error al generar partidos de liga", e)
        }
    }

    private fun generateRoundRobinRounds(teams: List<TeamInput>): List<List<Pair<TeamInput, TeamInput>>> {
        val list = teams.toMutableList()
        val numTeams = list.size
        val hasGhost = numTeams % 2 != 0
        val rotationSize = if (hasGhost) numTeams + 1 else numTeams
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

    fun finishMatchesRandomly(leagueId: Int? = null, tournamentId: Int? = null) = viewModelScope.launch {
        val matchesToFinish = allMatches.value.filter { 
            (it.leagueId == leagueId || leagueId == null) && 
            (it.tournamentId == tournamentId || tournamentId == null) && 
            it.playFinish == null 
        }
        
        if (matchesToFinish.isEmpty()) return@launch

        matchesToFinish.forEach { match ->
            val updatedMatch = if (match.courtNumber == 0) {
                // En el caso de RESERVA (Pista 0), siempre gana el equipo/jugador en espera (Team 1)
                match.copy(
                    scoreTeamOne = 2,
                    scoreTeamTwo = 0,
                    playFinish = System.currentTimeMillis()
                )
            } else {
                val winner = (1..2).random()
                val isTwoZero = (0..1).random() == 0
                
                if (winner == 1) {
                    match.copy(
                        scoreTeamOne = 2,
                        scoreTeamTwo = if (isTwoZero) 0 else 1,
                        playFinish = System.currentTimeMillis()
                    )
                } else {
                    match.copy(
                        scoreTeamOne = if (isTwoZero) 0 else 1,
                        scoreTeamTwo = 2,
                        playFinish = System.currentTimeMillis()
                    )
                }
            }
            repository.updateMatch(updatedMatch)
        }

        leagueId?.let { lid ->
            val matches = repository.getMatchesByLeagueSync(lid)
            if (matches.all { it.playFinish != null }) {
                val currentLeague = allLeagues.value.find { it.id == lid }
                currentLeague?.let { repository.update(it.copy(isFinished = true)) }
            }
            calculateRankings(lid)
        }
        
        tournamentId?.let { tid ->
            // Para torneos normales, simplemente calculamos el ranking
            calculateTournamentRankingsShared(tid)
        }
    }

    private suspend fun calculateRankings(leagueId: Int) {
        val allLeagueMatches = repository.getMatchesByLeagueSync(leagueId).filter { it.playFinish != null }
        val playerStats = mutableMapOf<Int, Ranking>()
        
        allLeagueMatches.forEach { m ->
            val pIds = listOf(m.player1Id, m.player2Id, m.player3Id, m.player4Id)
            pIds.forEach { pid -> if (!playerStats.containsKey(pid)) playerStats[pid] = Ranking(leagueId = leagueId, playerId = pid) }
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
        repository.updateRankings(playerStats.values.toList())
    }
    
    private suspend fun calculateTournamentRankingsShared(tournamentId: Int) {
        val matches = repository.getMatchesByTournamentSync(tournamentId).filter { it.playFinish != null }
        val playerStats = mutableMapOf<Int, Ranking>()
        matches.forEach { m ->
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
        repository.updateRankings(playerStats.values.toList())
    }

    fun resetGeneralRanking() = viewModelScope.launch {
        repository.deleteAllRankings()
    }

    fun resetDatabase() = viewModelScope.launch {}
}

class LeagueViewModelFactory(private val repository: LeagueRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeagueViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeagueViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
