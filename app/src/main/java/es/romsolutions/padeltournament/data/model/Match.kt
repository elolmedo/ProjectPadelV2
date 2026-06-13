package es.romsolutions.padeltournament.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tournamentId: Int? = null,
    val leagueId: Int? = null,
    val player1Id: Int = 0,
    val player2Id: Int = 0,
    val player3Id: Int = 0,
    val player4Id: Int = 0,
    val teamOneId: Int = 0,
    val teamTwoId: Int = 0,
    val isByTime: Boolean = true,
    val playStart: Long? = null,
    val playFinish: Long? = null,
    val scoreTeamOne: Int = 0,
    val scoreTeamTwo: Int = 0,
    val courtNumber: Int = 1,
    val weekNumber: Int = 1,
    val roundNumber: Int = 1
)
