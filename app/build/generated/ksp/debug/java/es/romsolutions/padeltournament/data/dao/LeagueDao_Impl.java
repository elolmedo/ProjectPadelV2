package es.romsolutions.padeltournament.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import es.romsolutions.padeltournament.data.model.League;
import es.romsolutions.padeltournament.data.model.LeaguePlayerCrossRef;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LeagueDao_Impl implements LeagueDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<League> __insertionAdapterOfLeague;

  private final EntityInsertionAdapter<LeaguePlayerCrossRef> __insertionAdapterOfLeaguePlayerCrossRef;

  private final EntityDeletionOrUpdateAdapter<League> __deletionAdapterOfLeague;

  private final EntityDeletionOrUpdateAdapter<League> __updateAdapterOfLeague;

  public LeagueDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLeague = new EntityInsertionAdapter<League>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `leagues` (`id`,`name`,`size`,`weeklyMatches`,`startDate`,`endDate`,`score`,`isStarted`,`matchDays`,`isTeamBased`,`isFinished`,`numberCourts`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final League entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getSize());
        statement.bindLong(4, entity.getWeeklyMatches());
        statement.bindLong(5, entity.getStartDate());
        statement.bindLong(6, entity.getEndDate());
        statement.bindLong(7, entity.getScore());
        final int _tmp = entity.isStarted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getMatchDays());
        final int _tmp_1 = entity.isTeamBased() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        final int _tmp_2 = entity.isFinished() ? 1 : 0;
        statement.bindLong(11, _tmp_2);
        statement.bindLong(12, entity.getNumberCourts());
      }
    };
    this.__insertionAdapterOfLeaguePlayerCrossRef = new EntityInsertionAdapter<LeaguePlayerCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `league_players` (`leagueId`,`playerId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LeaguePlayerCrossRef entity) {
        statement.bindLong(1, entity.getLeagueId());
        statement.bindLong(2, entity.getPlayerId());
      }
    };
    this.__deletionAdapterOfLeague = new EntityDeletionOrUpdateAdapter<League>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `leagues` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final League entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfLeague = new EntityDeletionOrUpdateAdapter<League>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `leagues` SET `id` = ?,`name` = ?,`size` = ?,`weeklyMatches` = ?,`startDate` = ?,`endDate` = ?,`score` = ?,`isStarted` = ?,`matchDays` = ?,`isTeamBased` = ?,`isFinished` = ?,`numberCourts` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final League entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getSize());
        statement.bindLong(4, entity.getWeeklyMatches());
        statement.bindLong(5, entity.getStartDate());
        statement.bindLong(6, entity.getEndDate());
        statement.bindLong(7, entity.getScore());
        final int _tmp = entity.isStarted() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getMatchDays());
        final int _tmp_1 = entity.isTeamBased() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        final int _tmp_2 = entity.isFinished() ? 1 : 0;
        statement.bindLong(11, _tmp_2);
        statement.bindLong(12, entity.getNumberCourts());
        statement.bindLong(13, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final League league, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfLeague.insertAndReturnId(league);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLeaguePlayers(final List<LeaguePlayerCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLeaguePlayerCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final League league, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfLeague.handle(league);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final League league, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLeague.handle(league);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLeagueWithPlayers(final League league, final List<Integer> playerIds,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> LeagueDao.DefaultImpls.insertLeagueWithPlayers(LeagueDao_Impl.this, league, playerIds, __cont), $completion);
  }

  @Override
  public Flow<List<League>> getAllLeagues() {
    final String _sql = "SELECT * FROM leagues ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"leagues"}, new Callable<List<League>>() {
      @Override
      @NonNull
      public List<League> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
          final int _cursorIndexOfWeeklyMatches = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyMatches");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfIsStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "isStarted");
          final int _cursorIndexOfMatchDays = CursorUtil.getColumnIndexOrThrow(_cursor, "matchDays");
          final int _cursorIndexOfIsTeamBased = CursorUtil.getColumnIndexOrThrow(_cursor, "isTeamBased");
          final int _cursorIndexOfIsFinished = CursorUtil.getColumnIndexOrThrow(_cursor, "isFinished");
          final int _cursorIndexOfNumberCourts = CursorUtil.getColumnIndexOrThrow(_cursor, "numberCourts");
          final List<League> _result = new ArrayList<League>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final League _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpSize;
            _tmpSize = _cursor.getInt(_cursorIndexOfSize);
            final int _tmpWeeklyMatches;
            _tmpWeeklyMatches = _cursor.getInt(_cursorIndexOfWeeklyMatches);
            final long _tmpStartDate;
            _tmpStartDate = _cursor.getLong(_cursorIndexOfStartDate);
            final long _tmpEndDate;
            _tmpEndDate = _cursor.getLong(_cursorIndexOfEndDate);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final boolean _tmpIsStarted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsStarted);
            _tmpIsStarted = _tmp != 0;
            final String _tmpMatchDays;
            _tmpMatchDays = _cursor.getString(_cursorIndexOfMatchDays);
            final boolean _tmpIsTeamBased;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsTeamBased);
            _tmpIsTeamBased = _tmp_1 != 0;
            final boolean _tmpIsFinished;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFinished);
            _tmpIsFinished = _tmp_2 != 0;
            final int _tmpNumberCourts;
            _tmpNumberCourts = _cursor.getInt(_cursorIndexOfNumberCourts);
            _item = new League(_tmpId,_tmpName,_tmpSize,_tmpWeeklyMatches,_tmpStartDate,_tmpEndDate,_tmpScore,_tmpIsStarted,_tmpMatchDays,_tmpIsTeamBased,_tmpIsFinished,_tmpNumberCourts);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPlayersInLeague(final int leagueId,
      final Continuation<? super List<Integer>> $completion) {
    final String _sql = "SELECT playerId FROM league_players WHERE leagueId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, leagueId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Integer>>() {
      @Override
      @NonNull
      public List<Integer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Integer _item;
            _item = _cursor.getInt(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
