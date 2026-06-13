package es.romsolutions.padeltournament.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import es.romsolutions.padeltournament.data.dao.LeagueDao
import es.romsolutions.padeltournament.data.dao.MatchDao
import es.romsolutions.padeltournament.data.dao.PlayerDao
import es.romsolutions.padeltournament.data.dao.RankingDao
import es.romsolutions.padeltournament.data.dao.TeamDao
import es.romsolutions.padeltournament.data.dao.TournamentDao
import es.romsolutions.padeltournament.data.model.League
import es.romsolutions.padeltournament.data.model.LeaguePlayerCrossRef
import es.romsolutions.padeltournament.data.model.Match
import es.romsolutions.padeltournament.data.model.Player
import es.romsolutions.padeltournament.data.model.Ranking
import es.romsolutions.padeltournament.data.model.Team
import es.romsolutions.padeltournament.data.model.Tournament
import es.romsolutions.padeltournament.data.model.TournamentPlayerCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Database(
    entities = [
        Player::class, 
        Tournament::class, 
        Team::class, 
        Match::class, 
        League::class, 
        LeaguePlayerCrossRef::class,
        Ranking::class,
        TournamentPlayerCrossRef::class
    ],
    version = 24,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun tournamentDao(): TournamentDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
    abstract fun leagueDao(): LeagueDao
    abstract fun rankingDao(): RankingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance: AppDatabase? = null
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "padel_database"
                )
                .addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Verificamos e insertamos si está vacío CADA VEZ que se abre
                        instance?.let { database ->
                            DatabaseSeeder.seedPlayersIfEmpty(database.playerDao(), applicationScope)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
