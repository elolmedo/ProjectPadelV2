package es.romsolutions.padeltournament.data.database

import android.util.Log
import es.romsolutions.padeltournament.data.dao.PlayerDao
import es.romsolutions.padeltournament.data.model.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseSeeder {
    private const val TAG = "DatabaseSeeder"

    fun seedPlayersIfEmpty(playerDao: PlayerDao, scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            try {
                val count = playerDao.getPlayerCount()
                Log.d(TAG, "Checking player count: $count")
                
                if (count == 0) {
                    Log.d(TAG, "Seeding 50 players...")
                    val nombres = listOf("Juan", "María", "Pedro", "Ana", "Carlos", "Lucía", "Javier", "Elena", "Pablo", "Sonia", "Raúl", "Marta", "Diego", "Carmen", "Luis", "Isabel", "Jorge", "Laura", "Alberto", "Rosa")
                    val apellidos = listOf("García", "Rodríguez", "González", "Fernández", "López", "Martínez", "Sánchez", "Pérez", "Gómez", "Martín", "Jiménez", "Ruiz", "Hernández", "Diaz", "Moreno", "Muñoz", "Álvarez", "Romero", "Alonso", "Gutiérrez")
                    
                    val randomPlayers = (1..50).map { i ->
                        val nombre = nombres.random()
                        val apellido = apellidos.random()
                        val sexo = if (i % 2 == 0) "M" else "F"
                        Player(
                            nombre = "$nombre $apellido",
                            sexo = sexo,
                            phone = "6${(10000000..99999999).random()}",
                            email = "${nombre.lowercase()}.${apellido.lowercase()}$i@example.com",
                            adminId = null,
                            photoUri = null
                        )
                    }
                    
                    randomPlayers.forEach { playerDao.insertPlayer(it) }
                    Log.d(TAG, "Seeding completed successfully")
                } else {
                    Log.d(TAG, "Database already has players, skipping seed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error seeding players", e)
            }
        }
    }
}
