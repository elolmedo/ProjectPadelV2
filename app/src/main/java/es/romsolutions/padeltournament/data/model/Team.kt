package es.romsolutions.padeltournament.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "teams",
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
            childColumns = ["playerOneId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Player::class,
            parentColumns = ["idplayer"],
            childColumns = ["playerTwoId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = League::class,
            parentColumns = ["id"],
            childColumns = ["leagueId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tournamentId"), Index("playerOneId"), Index("playerTwoId"), Index("leagueId")]
)
data class Team(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tournamentId: Int? = null,
    val leagueId: Int? = null,
    val nameTeam: String,
    val playerOneId: Int,
    val playerTwoId: Int
)

data class TeamInput(val name: String, val player1Id: Int, val player2Id: Int)
