package es.romsolutions.padeltournament.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tournaments")
data class Tournament(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String = "AMERICANA", // "AMERICANA" o "EXPRESS"
    val scoreType: String = "TIME", // "TIME" o "SETS"
    val numSets: Int = 1, // 1 o 2
    val matchDuration: Int = 15, // en minutos: 10, 15, 20
    val isMixed: Boolean = true,
    val dateTour: Long? = null,
    val timeStart: Long? = null,
    val maxHoursPerDay: Int = 4,
    val numberPlayers: Int = 0,
    val numberCourts: Int = 0,
    val isTeamBased: Boolean = false,
    val isStarted: Boolean = false,
    val isFinished: Boolean = false,
    val adminId: String? = null
)

@Entity(
    tableName = "tournament_players",
    primaryKeys = ["tournamentId", "playerId"],
    foreignKeys = [
        ForeignKey(
            entity = Tournament::class,
            parentColumns = ["id"],
            childColumns = ["tournamentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Player::class,
            parentColumns = ["idplayer"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tournamentId"), Index("playerId")]
)
data class TournamentPlayerCrossRef(
    val tournamentId: Int,
    val playerId: Int
)
