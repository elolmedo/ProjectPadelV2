package es.romsolutions.padeltournament.data.repository

import es.romsolutions.padeltournament.data.dao.LeagueDao
import es.romsolutions.padeltournament.data.dao.MatchDao
import es.romsolutions.padeltournament.data.dao.RankingDao
import es.romsolutions.padeltournament.data.model.League
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.data.model.Ranking
import kotlinx.coroutines.flow.Flow

class LeagueRepository(
    private val leagueDao: LeagueDao,
    private val rankingDao: RankingDao,
    private val matchDao: MatchDao,
    private val teamDao: es.romsolutions.padeltournament.data.dao.TeamDao
) {
    fun getLeaguesByAdmin(adminId: String?): Flow<List<League>> = leagueDao.getLeaguesByAdmin(adminId)
    val allMatches: Flow<List<Match>> = matchDao.getAllMatches()
    val allTeams: Flow<List<es.romsolutions.padeltournament.data.model.Team>> = teamDao.getAllTeams()
    val generalRanking: Flow<List<Ranking>> = rankingDao.getGeneralRanking()

    fun getRankingForLeague(leagueId: Int): Flow<List<Ranking>> {
        return rankingDao.getRankingForLeague(leagueId)
    }

    suspend fun updateRankings(rankings: List<Ranking>) {
        rankingDao.insertAll(rankings)
    }

    suspend fun insertLeagueWithPlayersAndRanking(league: League, playerIds: List<Int>) {
        val leagueId = leagueDao.insert(league).toInt()
        
        val crossRefs = playerIds.map { es.romsolutions.padeltournament.data.model.LeaguePlayerCrossRef(leagueId, it) }
        leagueDao.insertLeaguePlayers(crossRefs)
        
        val initialRankings = playerIds.map { playerId ->
            Ranking(leagueId = leagueId, playerId = playerId)
        }
        rankingDao.insertAll(initialRankings)
    }

    suspend fun getPlayersInLeague(leagueId: Int): List<Int> {
        return leagueDao.getPlayersInLeague(leagueId)
    }

    suspend fun startLeagueWithMatchesAndTeams(league: League, matches: List<Match>, teams: List<es.romsolutions.padeltournament.data.model.Team>): List<Long> {
        val teamIds = teamDao.insertAll(teams)
        matchDao.insertAll(matches)
        leagueDao.update(league)
        return teamIds
    }

    suspend fun startLeagueWithMatches(league: League, matches: List<Match>) {
        matchDao.insertAll(matches)
        leagueDao.update(league)
    }

    suspend fun insert(league: League) {
        leagueDao.insert(league)
    }

    suspend fun update(league: League) {
        leagueDao.update(league)
    }

    suspend fun updateMatch(match: Match) {
        matchDao.update(match)
    }

    suspend fun getMatchesByLeagueSync(leagueId: Int): List<Match> {
        return matchDao.getMatchesByLeagueSync(leagueId)
    }

    suspend fun getMatchesByTournamentSync(tournamentId: Int): List<Match> {
        return matchDao.getMatchesByTournamentSync(tournamentId)
    }

    suspend fun delete(league: League) {
        leagueDao.delete(league)
    }

    suspend fun deleteAllRankings() {
        rankingDao.deleteAll()
    }
}
