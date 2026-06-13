package es.romsolutions.padeltournament.data.dao

import androidx.room.*
import es.romsolutions.padeltournament.data.model.Ranking
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingDao {
    @Query("SELECT * FROM rankings WHERE leagueId = :leagueId ORDER BY points DESC, matchesWon DESC")
    fun getRankingForLeague(leagueId: Int): Flow<List<Ranking>>

    @Query("SELECT playerId, SUM(points) as points, SUM(matchesPlayed) as matchesPlayed, SUM(matchesWon) as matchesWon, SUM(matchesLost) as matchesLost, 0 as leagueId, 0 as position FROM rankings GROUP BY playerId ORDER BY points DESC")
    fun getGeneralRanking(): Flow<List<Ranking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ranking: Ranking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rankings: List<Ranking>)

    @Update
    suspend fun update(ranking: Ranking)

    @Query("DELETE FROM rankings")
    suspend fun deleteAll()
}
