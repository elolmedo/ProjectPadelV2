package es.romsolutions.padeltournament.data.repository

import es.romsolutions.padeltournament.data.dao.PlayerDao
import es.romsolutions.padeltournament.data.model.Player
import kotlinx.coroutines.flow.Flow

class PlayerRepository(val playerDao: PlayerDao) {
    val allPlayers: Flow<List<Player>> = playerDao.getAllPlayers()

    suspend fun insert(player: Player) {
        playerDao.insertPlayer(player)
    }

    suspend fun delete(id: Int) {
        playerDao.deletePlayerById(id)
    }

    suspend fun deleteAll() {
        playerDao.deleteAllPlayers()
    }
}
