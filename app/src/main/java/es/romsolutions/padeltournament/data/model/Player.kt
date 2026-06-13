package es.romsolutions.padeltournament.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idplayer")
    val id: Int = 0,
    
    @ColumnInfo(name = "name")
    val nombre: String = "",
    
    @ColumnInfo(name = "sex")
    val sexo: String = "", // 'M' o 'F'
    
    @ColumnInfo(name = "mail")
    val email: String = "",
    
    @ColumnInfo(name = "phone")
    val phone: String = "",
    
    @ColumnInfo(name = "numbertorneosparticipate")
    val tournamentsPlayed: Int = 0,
    
    @ColumnInfo(name = "wintournaments")
    val tournamentsWon: Int = 0,
    
    @ColumnInfo(name = "setplayed")
    val setsPlayed: Int = 0,
    
    @ColumnInfo(name = "setwinner")
    val setsWon: Int = 0,
    
    @ColumnInfo(name = "adminid")
    val adminId: Int = 0
)
