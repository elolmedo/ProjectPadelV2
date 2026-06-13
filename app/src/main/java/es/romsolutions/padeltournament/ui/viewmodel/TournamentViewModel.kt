package es.romsolutions.padeltournament.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.data.model.Tournament
import es.romsolutions.padeltournament.data.model.Ranking
import es.romsolutions.padeltournament.data.repository.TournamentRepository
import es.romsolutions.padeltournament.data.repository.LeagueRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class TournamentViewModel(
    private val repository: TournamentRepository,
    private val leagueRepository: LeagueRepository
) : ViewModel() {
    private val TAG = "TournamentViewModel"

    val allTournaments: StateFlow<List<Tournament>> = repository.allTournaments
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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
            if (playerIds.size < 4) return@launch

            val matches = if (tournament.type == "AMERICANA") {
                generateAmericanaRotation(tournament, playerIds)
            } else {
                generateExpressMatches(tournament, playerIds)
            }
            
            repository.startTournamentWithMatches(tournament.copy(isStarted = true), matches)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting tournament", e)
        }
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
        val teams = (0 until numTeams).map { i ->
            Pair(players[i * 2], players[i * 2 + 1])
        }

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
            // Generar un bloque de partidos simultáneos (uno por pista)
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
            
            // Siguiente turno: duración del partido + 5 min de descanso/cambio
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
        val allMatchesFlow = leagueRepository.allMatches.first()
        val tournamentMatches = allMatchesFlow.filter { it.tournamentId == tournamentId && it.playFinish != null }
        
        val playerStats = mutableMapOf<Int, Ranking>()
        
        tournamentMatches.forEach { m ->
            val pIds = listOf(m.player1Id, m.player2Id, m.player3Id, m.player4Id)
            pIds.forEach { pid ->
                if (!playerStats.containsKey(pid)) {
                    // Usamos ID negativo de forma unificada: - (tournamentId + offset gigante) para evitar colisión con ligas
                    // Pero mejor aún, usamos el leagueId del Ranking para guardar el ID del torneo
                    // IMPORTANTE: Para que aparezca en el filtro "Por Torneo", el leagueId debe ser el ID del torneo
                    // pero necesitamos distinguirlo de las ligas. Usaremos el valor negativo del ID del torneo.
                    playerStats[pid] = Ranking(leagueId = -tournamentId, playerId = pid)
                }
            }
            
            val s1 = playerStats[m.player1Id]!!
            val s2 = playerStats[m.player2Id]!!
            val s3 = playerStats[m.player3Id]!!
            val s4 = playerStats[m.player4Id]!!

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
}

class TournamentViewModelFactory(
    private val repository: TournamentRepository,
    private val leagueRepository: LeagueRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TournamentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TournamentViewModel(repository, leagueRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
