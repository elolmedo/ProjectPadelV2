package es.romsolutions.padeltournament.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import es.romsolutions.padeltournament.data.dao.LeagueDao;
import es.romsolutions.padeltournament.data.dao.LeagueDao_Impl;
import es.romsolutions.padeltournament.data.dao.MatchDao;
import es.romsolutions.padeltournament.data.dao.MatchDao_Impl;
import es.romsolutions.padeltournament.data.dao.PlayerDao;
import es.romsolutions.padeltournament.data.dao.PlayerDao_Impl;
import es.romsolutions.padeltournament.data.dao.RankingDao;
import es.romsolutions.padeltournament.data.dao.RankingDao_Impl;
import es.romsolutions.padeltournament.data.dao.TeamDao;
import es.romsolutions.padeltournament.data.dao.TeamDao_Impl;
import es.romsolutions.padeltournament.data.dao.TournamentDao;
import es.romsolutions.padeltournament.data.dao.TournamentDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PlayerDao _playerDao;

  private volatile TournamentDao _tournamentDao;

  private volatile TeamDao _teamDao;

  private volatile MatchDao _matchDao;

  private volatile LeagueDao _leagueDao;

  private volatile RankingDao _rankingDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(24) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `players` (`idplayer` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `sex` TEXT NOT NULL, `mail` TEXT NOT NULL, `phone` TEXT NOT NULL, `numbertorneosparticipate` INTEGER NOT NULL, `wintournaments` INTEGER NOT NULL, `setplayed` INTEGER NOT NULL, `setwinner` INTEGER NOT NULL, `adminid` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tournaments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `scoreType` TEXT NOT NULL, `numSets` INTEGER NOT NULL, `matchDuration` INTEGER NOT NULL, `isMixed` INTEGER NOT NULL, `dateTour` INTEGER, `timeStart` INTEGER, `maxHoursPerDay` INTEGER NOT NULL, `numberPlayers` INTEGER NOT NULL, `numberCourts` INTEGER NOT NULL, `isStarted` INTEGER NOT NULL, `isFinished` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `teams` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tournamentId` INTEGER, `leagueId` INTEGER, `nameTeam` TEXT NOT NULL, `playerOneId` INTEGER NOT NULL, `playerTwoId` INTEGER NOT NULL, FOREIGN KEY(`tournamentId`) REFERENCES `tournaments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`playerOneId`) REFERENCES `players`(`idplayer`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`playerTwoId`) REFERENCES `players`(`idplayer`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`leagueId`) REFERENCES `leagues`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_teams_tournamentId` ON `teams` (`tournamentId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_teams_playerOneId` ON `teams` (`playerOneId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_teams_playerTwoId` ON `teams` (`playerTwoId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_teams_leagueId` ON `teams` (`leagueId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `matches` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tournamentId` INTEGER, `leagueId` INTEGER, `player1Id` INTEGER NOT NULL, `player2Id` INTEGER NOT NULL, `player3Id` INTEGER NOT NULL, `player4Id` INTEGER NOT NULL, `teamOneId` INTEGER NOT NULL, `teamTwoId` INTEGER NOT NULL, `isByTime` INTEGER NOT NULL, `playStart` INTEGER, `playFinish` INTEGER, `scoreTeamOne` INTEGER NOT NULL, `scoreTeamTwo` INTEGER NOT NULL, `courtNumber` INTEGER NOT NULL, `weekNumber` INTEGER NOT NULL, `roundNumber` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `leagues` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `size` INTEGER NOT NULL, `weeklyMatches` INTEGER NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `score` INTEGER NOT NULL, `isStarted` INTEGER NOT NULL, `matchDays` TEXT NOT NULL, `isTeamBased` INTEGER NOT NULL, `isFinished` INTEGER NOT NULL, `numberCourts` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `league_players` (`leagueId` INTEGER NOT NULL, `playerId` INTEGER NOT NULL, PRIMARY KEY(`leagueId`, `playerId`), FOREIGN KEY(`leagueId`) REFERENCES `leagues`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`playerId`) REFERENCES `players`(`idplayer`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_league_players_leagueId` ON `league_players` (`leagueId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_league_players_playerId` ON `league_players` (`playerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rankings` (`leagueId` INTEGER NOT NULL, `playerId` INTEGER NOT NULL, `points` INTEGER NOT NULL, `matchesPlayed` INTEGER NOT NULL, `matchesWon` INTEGER NOT NULL, `matchesLost` INTEGER NOT NULL, `position` INTEGER NOT NULL, PRIMARY KEY(`leagueId`, `playerId`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rankings_leagueId` ON `rankings` (`leagueId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rankings_playerId` ON `rankings` (`playerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tournament_players` (`tournamentId` INTEGER NOT NULL, `playerId` INTEGER NOT NULL, PRIMARY KEY(`tournamentId`, `playerId`), FOREIGN KEY(`tournamentId`) REFERENCES `tournaments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`playerId`) REFERENCES `players`(`idplayer`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tournament_players_tournamentId` ON `tournament_players` (`tournamentId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tournament_players_playerId` ON `tournament_players` (`playerId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3147d0bc48be521b8facb6f5e68ad883')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `players`");
        db.execSQL("DROP TABLE IF EXISTS `tournaments`");
        db.execSQL("DROP TABLE IF EXISTS `teams`");
        db.execSQL("DROP TABLE IF EXISTS `matches`");
        db.execSQL("DROP TABLE IF EXISTS `leagues`");
        db.execSQL("DROP TABLE IF EXISTS `league_players`");
        db.execSQL("DROP TABLE IF EXISTS `rankings`");
        db.execSQL("DROP TABLE IF EXISTS `tournament_players`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPlayers = new HashMap<String, TableInfo.Column>(10);
        _columnsPlayers.put("idplayer", new TableInfo.Column("idplayer", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("sex", new TableInfo.Column("sex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("mail", new TableInfo.Column("mail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("numbertorneosparticipate", new TableInfo.Column("numbertorneosparticipate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("wintournaments", new TableInfo.Column("wintournaments", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("setplayed", new TableInfo.Column("setplayed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("setwinner", new TableInfo.Column("setwinner", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("adminid", new TableInfo.Column("adminid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlayers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlayers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlayers = new TableInfo("players", _columnsPlayers, _foreignKeysPlayers, _indicesPlayers);
        final TableInfo _existingPlayers = TableInfo.read(db, "players");
        if (!_infoPlayers.equals(_existingPlayers)) {
          return new RoomOpenHelper.ValidationResult(false, "players(es.romsolutions.padeltournament.data.model.Player).\n"
                  + " Expected:\n" + _infoPlayers + "\n"
                  + " Found:\n" + _existingPlayers);
        }
        final HashMap<String, TableInfo.Column> _columnsTournaments = new HashMap<String, TableInfo.Column>(14);
        _columnsTournaments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("scoreType", new TableInfo.Column("scoreType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("numSets", new TableInfo.Column("numSets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("matchDuration", new TableInfo.Column("matchDuration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("isMixed", new TableInfo.Column("isMixed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("dateTour", new TableInfo.Column("dateTour", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("timeStart", new TableInfo.Column("timeStart", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("maxHoursPerDay", new TableInfo.Column("maxHoursPerDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("numberPlayers", new TableInfo.Column("numberPlayers", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("numberCourts", new TableInfo.Column("numberCourts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("isStarted", new TableInfo.Column("isStarted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournaments.put("isFinished", new TableInfo.Column("isFinished", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTournaments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTournaments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTournaments = new TableInfo("tournaments", _columnsTournaments, _foreignKeysTournaments, _indicesTournaments);
        final TableInfo _existingTournaments = TableInfo.read(db, "tournaments");
        if (!_infoTournaments.equals(_existingTournaments)) {
          return new RoomOpenHelper.ValidationResult(false, "tournaments(es.romsolutions.padeltournament.data.model.Tournament).\n"
                  + " Expected:\n" + _infoTournaments + "\n"
                  + " Found:\n" + _existingTournaments);
        }
        final HashMap<String, TableInfo.Column> _columnsTeams = new HashMap<String, TableInfo.Column>(6);
        _columnsTeams.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("tournamentId", new TableInfo.Column("tournamentId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("leagueId", new TableInfo.Column("leagueId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("nameTeam", new TableInfo.Column("nameTeam", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("playerOneId", new TableInfo.Column("playerOneId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTeams.put("playerTwoId", new TableInfo.Column("playerTwoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTeams = new HashSet<TableInfo.ForeignKey>(4);
        _foreignKeysTeams.add(new TableInfo.ForeignKey("tournaments", "CASCADE", "NO ACTION", Arrays.asList("tournamentId"), Arrays.asList("id")));
        _foreignKeysTeams.add(new TableInfo.ForeignKey("players", "RESTRICT", "NO ACTION", Arrays.asList("playerOneId"), Arrays.asList("idplayer")));
        _foreignKeysTeams.add(new TableInfo.ForeignKey("players", "RESTRICT", "NO ACTION", Arrays.asList("playerTwoId"), Arrays.asList("idplayer")));
        _foreignKeysTeams.add(new TableInfo.ForeignKey("leagues", "CASCADE", "NO ACTION", Arrays.asList("leagueId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTeams = new HashSet<TableInfo.Index>(4);
        _indicesTeams.add(new TableInfo.Index("index_teams_tournamentId", false, Arrays.asList("tournamentId"), Arrays.asList("ASC")));
        _indicesTeams.add(new TableInfo.Index("index_teams_playerOneId", false, Arrays.asList("playerOneId"), Arrays.asList("ASC")));
        _indicesTeams.add(new TableInfo.Index("index_teams_playerTwoId", false, Arrays.asList("playerTwoId"), Arrays.asList("ASC")));
        _indicesTeams.add(new TableInfo.Index("index_teams_leagueId", false, Arrays.asList("leagueId"), Arrays.asList("ASC")));
        final TableInfo _infoTeams = new TableInfo("teams", _columnsTeams, _foreignKeysTeams, _indicesTeams);
        final TableInfo _existingTeams = TableInfo.read(db, "teams");
        if (!_infoTeams.equals(_existingTeams)) {
          return new RoomOpenHelper.ValidationResult(false, "teams(es.romsolutions.padeltournament.data.model.Team).\n"
                  + " Expected:\n" + _infoTeams + "\n"
                  + " Found:\n" + _existingTeams);
        }
        final HashMap<String, TableInfo.Column> _columnsMatches = new HashMap<String, TableInfo.Column>(17);
        _columnsMatches.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("tournamentId", new TableInfo.Column("tournamentId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("leagueId", new TableInfo.Column("leagueId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("player1Id", new TableInfo.Column("player1Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("player2Id", new TableInfo.Column("player2Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("player3Id", new TableInfo.Column("player3Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("player4Id", new TableInfo.Column("player4Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("teamOneId", new TableInfo.Column("teamOneId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("teamTwoId", new TableInfo.Column("teamTwoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("isByTime", new TableInfo.Column("isByTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("playStart", new TableInfo.Column("playStart", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("playFinish", new TableInfo.Column("playFinish", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("scoreTeamOne", new TableInfo.Column("scoreTeamOne", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("scoreTeamTwo", new TableInfo.Column("scoreTeamTwo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("courtNumber", new TableInfo.Column("courtNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("weekNumber", new TableInfo.Column("weekNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("roundNumber", new TableInfo.Column("roundNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMatches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMatches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMatches = new TableInfo("matches", _columnsMatches, _foreignKeysMatches, _indicesMatches);
        final TableInfo _existingMatches = TableInfo.read(db, "matches");
        if (!_infoMatches.equals(_existingMatches)) {
          return new RoomOpenHelper.ValidationResult(false, "matches(es.romsolutions.padeltournament.data.model.Match).\n"
                  + " Expected:\n" + _infoMatches + "\n"
                  + " Found:\n" + _existingMatches);
        }
        final HashMap<String, TableInfo.Column> _columnsLeagues = new HashMap<String, TableInfo.Column>(12);
        _columnsLeagues.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("size", new TableInfo.Column("size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("weeklyMatches", new TableInfo.Column("weeklyMatches", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("startDate", new TableInfo.Column("startDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("endDate", new TableInfo.Column("endDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("isStarted", new TableInfo.Column("isStarted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("matchDays", new TableInfo.Column("matchDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("isTeamBased", new TableInfo.Column("isTeamBased", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("isFinished", new TableInfo.Column("isFinished", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeagues.put("numberCourts", new TableInfo.Column("numberCourts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLeagues = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLeagues = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLeagues = new TableInfo("leagues", _columnsLeagues, _foreignKeysLeagues, _indicesLeagues);
        final TableInfo _existingLeagues = TableInfo.read(db, "leagues");
        if (!_infoLeagues.equals(_existingLeagues)) {
          return new RoomOpenHelper.ValidationResult(false, "leagues(es.romsolutions.padeltournament.data.model.League).\n"
                  + " Expected:\n" + _infoLeagues + "\n"
                  + " Found:\n" + _existingLeagues);
        }
        final HashMap<String, TableInfo.Column> _columnsLeaguePlayers = new HashMap<String, TableInfo.Column>(2);
        _columnsLeaguePlayers.put("leagueId", new TableInfo.Column("leagueId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLeaguePlayers.put("playerId", new TableInfo.Column("playerId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLeaguePlayers = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysLeaguePlayers.add(new TableInfo.ForeignKey("leagues", "CASCADE", "NO ACTION", Arrays.asList("leagueId"), Arrays.asList("id")));
        _foreignKeysLeaguePlayers.add(new TableInfo.ForeignKey("players", "CASCADE", "NO ACTION", Arrays.asList("playerId"), Arrays.asList("idplayer")));
        final HashSet<TableInfo.Index> _indicesLeaguePlayers = new HashSet<TableInfo.Index>(2);
        _indicesLeaguePlayers.add(new TableInfo.Index("index_league_players_leagueId", false, Arrays.asList("leagueId"), Arrays.asList("ASC")));
        _indicesLeaguePlayers.add(new TableInfo.Index("index_league_players_playerId", false, Arrays.asList("playerId"), Arrays.asList("ASC")));
        final TableInfo _infoLeaguePlayers = new TableInfo("league_players", _columnsLeaguePlayers, _foreignKeysLeaguePlayers, _indicesLeaguePlayers);
        final TableInfo _existingLeaguePlayers = TableInfo.read(db, "league_players");
        if (!_infoLeaguePlayers.equals(_existingLeaguePlayers)) {
          return new RoomOpenHelper.ValidationResult(false, "league_players(es.romsolutions.padeltournament.data.model.LeaguePlayerCrossRef).\n"
                  + " Expected:\n" + _infoLeaguePlayers + "\n"
                  + " Found:\n" + _existingLeaguePlayers);
        }
        final HashMap<String, TableInfo.Column> _columnsRankings = new HashMap<String, TableInfo.Column>(7);
        _columnsRankings.put("leagueId", new TableInfo.Column("leagueId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRankings.put("playerId", new TableInfo.Column("playerId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRankings.put("points", new TableInfo.Column("points", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRankings.put("matchesPlayed", new TableInfo.Column("matchesPlayed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRankings.put("matchesWon", new TableInfo.Column("matchesWon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRankings.put("matchesLost", new TableInfo.Column("matchesLost", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRankings.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRankings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRankings = new HashSet<TableInfo.Index>(2);
        _indicesRankings.add(new TableInfo.Index("index_rankings_leagueId", false, Arrays.asList("leagueId"), Arrays.asList("ASC")));
        _indicesRankings.add(new TableInfo.Index("index_rankings_playerId", false, Arrays.asList("playerId"), Arrays.asList("ASC")));
        final TableInfo _infoRankings = new TableInfo("rankings", _columnsRankings, _foreignKeysRankings, _indicesRankings);
        final TableInfo _existingRankings = TableInfo.read(db, "rankings");
        if (!_infoRankings.equals(_existingRankings)) {
          return new RoomOpenHelper.ValidationResult(false, "rankings(es.romsolutions.padeltournament.data.model.Ranking).\n"
                  + " Expected:\n" + _infoRankings + "\n"
                  + " Found:\n" + _existingRankings);
        }
        final HashMap<String, TableInfo.Column> _columnsTournamentPlayers = new HashMap<String, TableInfo.Column>(2);
        _columnsTournamentPlayers.put("tournamentId", new TableInfo.Column("tournamentId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTournamentPlayers.put("playerId", new TableInfo.Column("playerId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTournamentPlayers = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTournamentPlayers.add(new TableInfo.ForeignKey("tournaments", "CASCADE", "NO ACTION", Arrays.asList("tournamentId"), Arrays.asList("id")));
        _foreignKeysTournamentPlayers.add(new TableInfo.ForeignKey("players", "CASCADE", "NO ACTION", Arrays.asList("playerId"), Arrays.asList("idplayer")));
        final HashSet<TableInfo.Index> _indicesTournamentPlayers = new HashSet<TableInfo.Index>(2);
        _indicesTournamentPlayers.add(new TableInfo.Index("index_tournament_players_tournamentId", false, Arrays.asList("tournamentId"), Arrays.asList("ASC")));
        _indicesTournamentPlayers.add(new TableInfo.Index("index_tournament_players_playerId", false, Arrays.asList("playerId"), Arrays.asList("ASC")));
        final TableInfo _infoTournamentPlayers = new TableInfo("tournament_players", _columnsTournamentPlayers, _foreignKeysTournamentPlayers, _indicesTournamentPlayers);
        final TableInfo _existingTournamentPlayers = TableInfo.read(db, "tournament_players");
        if (!_infoTournamentPlayers.equals(_existingTournamentPlayers)) {
          return new RoomOpenHelper.ValidationResult(false, "tournament_players(es.romsolutions.padeltournament.data.model.TournamentPlayerCrossRef).\n"
                  + " Expected:\n" + _infoTournamentPlayers + "\n"
                  + " Found:\n" + _existingTournamentPlayers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3147d0bc48be521b8facb6f5e68ad883", "b0358bd290abb22bac17429762ffe335");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "players","tournaments","teams","matches","leagues","league_players","rankings","tournament_players");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `players`");
      _db.execSQL("DELETE FROM `tournaments`");
      _db.execSQL("DELETE FROM `teams`");
      _db.execSQL("DELETE FROM `matches`");
      _db.execSQL("DELETE FROM `leagues`");
      _db.execSQL("DELETE FROM `league_players`");
      _db.execSQL("DELETE FROM `rankings`");
      _db.execSQL("DELETE FROM `tournament_players`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PlayerDao.class, PlayerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TournamentDao.class, TournamentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TeamDao.class, TeamDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MatchDao.class, MatchDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LeagueDao.class, LeagueDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RankingDao.class, RankingDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PlayerDao playerDao() {
    if (_playerDao != null) {
      return _playerDao;
    } else {
      synchronized(this) {
        if(_playerDao == null) {
          _playerDao = new PlayerDao_Impl(this);
        }
        return _playerDao;
      }
    }
  }

  @Override
  public TournamentDao tournamentDao() {
    if (_tournamentDao != null) {
      return _tournamentDao;
    } else {
      synchronized(this) {
        if(_tournamentDao == null) {
          _tournamentDao = new TournamentDao_Impl(this);
        }
        return _tournamentDao;
      }
    }
  }

  @Override
  public TeamDao teamDao() {
    if (_teamDao != null) {
      return _teamDao;
    } else {
      synchronized(this) {
        if(_teamDao == null) {
          _teamDao = new TeamDao_Impl(this);
        }
        return _teamDao;
      }
    }
  }

  @Override
  public MatchDao matchDao() {
    if (_matchDao != null) {
      return _matchDao;
    } else {
      synchronized(this) {
        if(_matchDao == null) {
          _matchDao = new MatchDao_Impl(this);
        }
        return _matchDao;
      }
    }
  }

  @Override
  public LeagueDao leagueDao() {
    if (_leagueDao != null) {
      return _leagueDao;
    } else {
      synchronized(this) {
        if(_leagueDao == null) {
          _leagueDao = new LeagueDao_Impl(this);
        }
        return _leagueDao;
      }
    }
  }

  @Override
  public RankingDao rankingDao() {
    if (_rankingDao != null) {
      return _rankingDao;
    } else {
      synchronized(this) {
        if(_rankingDao == null) {
          _rankingDao = new RankingDao_Impl(this);
        }
        return _rankingDao;
      }
    }
  }
}
