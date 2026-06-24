package es.romsolutions.padeltournament.data.dao

import androidx.room.*
import es.romsolutions.padeltournament.data.model.Match
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches WHERE tournamentId = :tournamentId")
    fun getMatchesByTournament(tournamentId: Int): Flow<List<Match>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(match: Match)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<Match>)

    @Query("SELECT * FROM matches WHERE leagueId = :leagueId")
    fun getMatchesByLeague(leagueId: Int): Flow<List<Match>>

    @Query("SELECT * FROM matches ORDER BY playStart ASC")
    fun getAllMatches(): Flow<List<Match>>

    @Update
    suspend fun update(match: Match)

    @Query("SELECT * FROM matches WHERE tournamentId = :tournamentId")
    suspend fun getMatchesByTournamentSync(tournamentId: Int): List<Match>

    @Query("SELECT * FROM matches WHERE leagueId = :leagueId")
    suspend fun getMatchesByLeagueSync(leagueId: Int): List<Match>

    @Delete
    suspend fun delete(match: Match)
}
