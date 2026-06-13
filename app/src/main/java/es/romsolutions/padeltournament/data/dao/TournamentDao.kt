package es.romsolutions.padeltournament.data.dao

import androidx.room.*
import es.romsolutions.padeltournament.data.model.Tournament
import es.romsolutions.padeltournament.data.model.TournamentPlayerCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {
    @Query("SELECT * FROM tournaments ORDER BY dateTour DESC")
    fun getAllTournaments(): Flow<List<Tournament>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tournament: Tournament): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTournamentPlayers(crossRefs: List<TournamentPlayerCrossRef>)

    @Transaction
    suspend fun insertTournamentWithPlayers(tournament: Tournament, playerIds: List<Int>) {
        val tId = insert(tournament).toInt()
        val crossRefs = playerIds.map { TournamentPlayerCrossRef(tId, it) }
        insertTournamentPlayers(crossRefs)
    }

    @Update
    suspend fun update(tournament: Tournament)

    @Delete
    suspend fun delete(tournament: Tournament)

    @Query("SELECT * FROM tournaments WHERE id = :id")
    suspend fun getTournamentById(id: Int): Tournament?

    @Query("SELECT playerId FROM tournament_players WHERE tournamentId = :tournamentId")
    suspend fun getPlayersInTournament(tournamentId: Int): List<Int>
}
