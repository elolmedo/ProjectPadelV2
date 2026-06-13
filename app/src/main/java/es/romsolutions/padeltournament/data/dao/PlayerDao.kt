package es.romsolutions.padeltournament.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.romsolutions.padeltournament.data.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players ORDER BY name ASC")
    fun getAllPlayers(): Flow<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Query("DELETE FROM players WHERE idplayer = :id")
    suspend fun deletePlayerById(id: Int)

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("SELECT COUNT(*) FROM players")
    suspend fun getPlayerCount(): Int
}
