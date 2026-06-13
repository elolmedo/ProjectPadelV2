package es.romsolutions.padeltournament.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "leagues")
data class League(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val size: Int, // Número de EQUIPOS que la componen
    val weeklyMatches: Int, // 1, 2, 3
    val startDate: Long, // Timestamp del inicio
    val endDate: Long, // Calculado
    val score: Int = 0,
    val isStarted: Boolean = false,
    val matchDays: String = "", // Guardaremos los días como "1,3,5" (L,X,V)
    val isTeamBased: Boolean = true, // Flag para diferenciar el tipo de liga
    val isFinished: Boolean = false,
    val numberCourts: Int = 1
)

@Entity(
    tableName = "league_players",
    primaryKeys = ["leagueId", "playerId"],
    foreignKeys = [
        ForeignKey(
            entity = League::class,
            parentColumns = ["id"],
            childColumns = ["leagueId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Player::class,
            parentColumns = ["idplayer"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("leagueId"), Index("playerId")]
)
data class LeaguePlayerCrossRef(
    val leagueId: Int,
    val playerId: Int
)
