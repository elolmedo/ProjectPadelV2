package es.romsolutions.padeltournament.data.dao

import androidx.room.*
import es.romsolutions.padeltournament.data.model.League
import es.romsolutions.padeltournament.data.model.LeaguePlayerCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface LeagueDao {
    @Query("SELECT * FROM leagues WHERE adminId = :adminId OR adminId IS NULL ORDER BY name ASC")
    fun getLeaguesByAdmin(adminId: String?): Flow<List<League>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(league: League): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaguePlayers(crossRefs: List<LeaguePlayerCrossRef>)

    @Transaction
    suspend fun insertLeagueWithPlayers(league: League, playerIds: List<Int>) {
        val leagueId = insert(league).toInt()
        val crossRefs = playerIds.map { LeaguePlayerCrossRef(leagueId, it) }
        insertLeaguePlayers(crossRefs)
    }

    @Update
    suspend fun update(league: League)

    @Delete
    suspend fun delete(league: League)

    @Query("SELECT playerId FROM league_players WHERE leagueId = :leagueId")
    suspend fun getPlayersInLeague(leagueId: Int): List<Int>
}
