package es.romsolutions.padeltournament.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "rankings",
    primaryKeys = ["leagueId", "playerId"],
    indices = [Index("leagueId"), Index("playerId")]
)
data class Ranking(
    val leagueId: Int, // Puede ser ID de Liga (positivo) o ID de Torneo (negativo)
    val playerId: Int,
    val points: Int = 0,
    val matchesPlayed: Int = 0,
    val matchesWon: Int = 0,
    val matchesLost: Int = 0,
    val position: Int = 0
)
