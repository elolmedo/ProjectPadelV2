package es.romsolutions.padeltournament.data.repository

import es.romsolutions.padeltournament.data.dao.MatchDao
import es.romsolutions.padeltournament.data.dao.TournamentDao
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.data.model.Tournament
import kotlinx.coroutines.flow.Flow

class TournamentRepository(
    private val tournamentDao: TournamentDao,
    private val matchDao: MatchDao
) {
    val allTournaments: Flow<List<Tournament>> = tournamentDao.getAllTournaments()

    suspend fun insert(tournament: Tournament) {
        tournamentDao.insert(tournament)
    }

    suspend fun insertTournamentWithPlayers(tournament: Tournament, playerIds: List<Int>) {
        tournamentDao.insertTournamentWithPlayers(tournament, playerIds)
    }

    suspend fun getPlayersInTournament(tournamentId: Int): List<Int> {
        return tournamentDao.getPlayersInTournament(tournamentId)
    }

    suspend fun startTournamentWithMatches(tournament: Tournament, matches: List<Match>) {
        matchDao.insertAll(matches)
        tournamentDao.update(tournament)
    }

    suspend fun update(tournament: Tournament) {
        tournamentDao.update(tournament)
    }

    suspend fun delete(tournament: Tournament) {
        tournamentDao.delete(tournament)
    }

    suspend fun getTournamentById(id: Int): Tournament? {
        return tournamentDao.getTournamentById(id)
    }
}
