package es.romsolutions.padeltournament.data.repository

import es.romsolutions.padeltournament.data.dao.PlayerDao
import es.romsolutions.padeltournament.data.model.Player
import kotlinx.coroutines.flow.Flow

class PlayerRepository(val playerDao: PlayerDao) {
    fun getPlayersByAdmin(adminId: String?): Flow<List<Player>> = playerDao.getPlayersByAdmin(adminId)

    suspend fun insert(player: Player) {
        playerDao.insertPlayer(player)
    }

    suspend fun delete(id: Int) {
        playerDao.deletePlayerById(id)
    }

    suspend fun deleteAll() {
        playerDao.deleteAllPlayers()
    }

    suspend fun getPlayersByIds(ids: List<Int>): List<Player> = playerDao.getPlayersByIds(ids)
}
