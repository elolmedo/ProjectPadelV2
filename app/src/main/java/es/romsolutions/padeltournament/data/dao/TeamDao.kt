package es.romsolutions.padeltournament.data.dao

import androidx.room.*
import es.romsolutions.padeltournament.data.model.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams WHERE tournamentId = :tournamentId")
    fun getTeamsByTournament(tournamentId: Int): Flow<List<Team>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(team: Team): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<Team>): List<Long>

    @Query("SELECT * FROM teams WHERE leagueId = :leagueId")
    fun getTeamsByLeague(leagueId: Int): Flow<List<Team>>

    @Query("SELECT * FROM teams")
    fun getAllTeams(): Flow<List<Team>>
}
