package es.romsolutions.padeltournament.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.romsolutions.padeltournament.data.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players WHERE adminid = :adminId OR adminid IS NULL ORDER BY name ASC")
    fun getPlayersByAdmin(adminId: String?): Flow<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Query("DELETE FROM players WHERE idplayer = :id")
    suspend fun deletePlayerById(id: Int)

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int

    @Query("SELECT * FROM players WHERE idplayer IN (:ids)")
    suspend fun getPlayersByIds(ids: List<Int>): List<Player>
}
