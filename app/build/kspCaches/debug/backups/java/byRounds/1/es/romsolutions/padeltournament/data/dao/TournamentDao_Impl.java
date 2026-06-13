package es.romsolutions.padeltournament.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import es.romsolutions.padeltournament.data.model.Tournament;
import es.romsolutions.padeltournament.data.model.TournamentPlayerCrossRef;
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
public final class TournamentDao_Impl implements TournamentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Tournament> __insertionAdapterOfTournament;

  private final EntityInsertionAdapter<TournamentPlayerCrossRef> __insertionAdapterOfTournamentPlayerCrossRef;

  private final EntityDeletionOrUpdateAdapter<Tournament> __deletionAdapterOfTournament;

  private final EntityDeletionOrUpdateAdapter<Tournament> __updateAdapterOfTournament;

  public TournamentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTournament = new EntityInsertionAdapter<Tournament>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tournaments` (`id`,`name`,`type`,`scoreType`,`numSets`,`matchDuration`,`isMixed`,`dateTour`,`timeStart`,`maxHoursPerDay`,`numberPlayers`,`numberCourts`,`isStarted`,`isFinished`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tournament entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getType());
        statement.bindString(4, entity.getScoreType());
        statement.bindLong(5, entity.getNumSets());
        statement.bindLong(6, entity.getMatchDuration());
        final int _tmp = entity.isMixed() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getDateTour() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getDateTour());
        }
        if (entity.getTimeStart() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getTimeStart());
        }
        statement.bindLong(10, entity.getMaxHoursPerDay());
        statement.bindLong(11, entity.getNumberPlayers());
        statement.bindLong(12, entity.getNumberCourts());
        final int _tmp_1 = entity.isStarted() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        final int _tmp_2 = entity.isFinished() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
      }
    };
    this.__insertionAdapterOfTournamentPlayerCrossRef = new EntityInsertionAdapter<TournamentPlayerCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tournament_players` (`tournamentId`,`playerId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TournamentPlayerCrossRef entity) {
        statement.bindLong(1, entity.getTournamentId());
        statement.bindLong(2, entity.getPlayerId());
      }
    };
    this.__deletionAdapterOfTournament = new EntityDeletionOrUpdateAdapter<Tournament>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tournaments` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tournament entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTournament = new EntityDeletionOrUpdateAdapter<Tournament>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tournaments` SET `id` = ?,`name` = ?,`type` = ?,`scoreType` = ?,`numSets` = ?,`matchDuration` = ?,`isMixed` = ?,`dateTour` = ?,`timeStart` = ?,`maxHoursPerDay` = ?,`numberPlayers` = ?,`numberCourts` = ?,`isStarted` = ?,`isFinished` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Tournament entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getType());
        statement.bindString(4, entity.getScoreType());
        statement.bindLong(5, entity.getNumSets());
        statement.bindLong(6, entity.getMatchDuration());
        final int _tmp = entity.isMixed() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getDateTour() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getDateTour());
        }
        if (entity.getTimeStart() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getTimeStart());
        }
        statement.bindLong(10, entity.getMaxHoursPerDay());
        statement.bindLong(11, entity.getNumberPlayers());
        statement.bindLong(12, entity.getNumberCourts());
        final int _tmp_1 = entity.isStarted() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        final int _tmp_2 = entity.isFinished() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
        statement.bindLong(15, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Tournament tournament, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTournament.insertAndReturnId(tournament);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTournamentPlayers(final List<TournamentPlayerCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTournamentPlayerCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Tournament tournament, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTournament.handle(tournament);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Tournament tournament, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTournament.handle(tournament);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTournamentWithPlayers(final Tournament tournament,
      final List<Integer> playerIds, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> TournamentDao.DefaultImpls.insertTournamentWithPlayers(TournamentDao_Impl.this, tournament, playerIds, __cont), $completion);
  }

  @Override
  public Flow<List<Tournament>> getAllTournaments() {
    final String _sql = "SELECT * FROM tournaments ORDER BY dateTour DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tournaments"}, new Callable<List<Tournament>>() {
      @Override
      @NonNull
      public List<Tournament> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfScoreType = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreType");
          final int _cursorIndexOfNumSets = CursorUtil.getColumnIndexOrThrow(_cursor, "numSets");
          final int _cursorIndexOfMatchDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "matchDuration");
          final int _cursorIndexOfIsMixed = CursorUtil.getColumnIndexOrThrow(_cursor, "isMixed");
          final int _cursorIndexOfDateTour = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTour");
          final int _cursorIndexOfTimeStart = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStart");
          final int _cursorIndexOfMaxHoursPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "maxHoursPerDay");
          final int _cursorIndexOfNumberPlayers = CursorUtil.getColumnIndexOrThrow(_cursor, "numberPlayers");
          final int _cursorIndexOfNumberCourts = CursorUtil.getColumnIndexOrThrow(_cursor, "numberCourts");
          final int _cursorIndexOfIsStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "isStarted");
          final int _cursorIndexOfIsFinished = CursorUtil.getColumnIndexOrThrow(_cursor, "isFinished");
          final List<Tournament> _result = new ArrayList<Tournament>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Tournament _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpScoreType;
            _tmpScoreType = _cursor.getString(_cursorIndexOfScoreType);
            final int _tmpNumSets;
            _tmpNumSets = _cursor.getInt(_cursorIndexOfNumSets);
            final int _tmpMatchDuration;
            _tmpMatchDuration = _cursor.getInt(_cursorIndexOfMatchDuration);
            final boolean _tmpIsMixed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsMixed);
            _tmpIsMixed = _tmp != 0;
            final Long _tmpDateTour;
            if (_cursor.isNull(_cursorIndexOfDateTour)) {
              _tmpDateTour = null;
            } else {
              _tmpDateTour = _cursor.getLong(_cursorIndexOfDateTour);
            }
            final Long _tmpTimeStart;
            if (_cursor.isNull(_cursorIndexOfTimeStart)) {
              _tmpTimeStart = null;
            } else {
              _tmpTimeStart = _cursor.getLong(_cursorIndexOfTimeStart);
            }
            final int _tmpMaxHoursPerDay;
            _tmpMaxHoursPerDay = _cursor.getInt(_cursorIndexOfMaxHoursPerDay);
            final int _tmpNumberPlayers;
            _tmpNumberPlayers = _cursor.getInt(_cursorIndexOfNumberPlayers);
            final int _tmpNumberCourts;
            _tmpNumberCourts = _cursor.getInt(_cursorIndexOfNumberCourts);
            final boolean _tmpIsStarted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsStarted);
            _tmpIsStarted = _tmp_1 != 0;
            final boolean _tmpIsFinished;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFinished);
            _tmpIsFinished = _tmp_2 != 0;
            _item = new Tournament(_tmpId,_tmpName,_tmpType,_tmpScoreType,_tmpNumSets,_tmpMatchDuration,_tmpIsMixed,_tmpDateTour,_tmpTimeStart,_tmpMaxHoursPerDay,_tmpNumberPlayers,_tmpNumberCourts,_tmpIsStarted,_tmpIsFinished);
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
  public Object getTournamentById(final int id,
      final Continuation<? super Tournament> $completion) {
    final String _sql = "SELECT * FROM tournaments WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Tournament>() {
      @Override
      @Nullable
      public Tournament call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfScoreType = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreType");
          final int _cursorIndexOfNumSets = CursorUtil.getColumnIndexOrThrow(_cursor, "numSets");
          final int _cursorIndexOfMatchDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "matchDuration");
          final int _cursorIndexOfIsMixed = CursorUtil.getColumnIndexOrThrow(_cursor, "isMixed");
          final int _cursorIndexOfDateTour = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTour");
          final int _cursorIndexOfTimeStart = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStart");
          final int _cursorIndexOfMaxHoursPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "maxHoursPerDay");
          final int _cursorIndexOfNumberPlayers = CursorUtil.getColumnIndexOrThrow(_cursor, "numberPlayers");
          final int _cursorIndexOfNumberCourts = CursorUtil.getColumnIndexOrThrow(_cursor, "numberCourts");
          final int _cursorIndexOfIsStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "isStarted");
          final int _cursorIndexOfIsFinished = CursorUtil.getColumnIndexOrThrow(_cursor, "isFinished");
          final Tournament _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpScoreType;
            _tmpScoreType = _cursor.getString(_cursorIndexOfScoreType);
            final int _tmpNumSets;
            _tmpNumSets = _cursor.getInt(_cursorIndexOfNumSets);
            final int _tmpMatchDuration;
            _tmpMatchDuration = _cursor.getInt(_cursorIndexOfMatchDuration);
            final boolean _tmpIsMixed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsMixed);
            _tmpIsMixed = _tmp != 0;
            final Long _tmpDateTour;
            if (_cursor.isNull(_cursorIndexOfDateTour)) {
              _tmpDateTour = null;
            } else {
              _tmpDateTour = _cursor.getLong(_cursorIndexOfDateTour);
            }
            final Long _tmpTimeStart;
            if (_cursor.isNull(_cursorIndexOfTimeStart)) {
              _tmpTimeStart = null;
            } else {
              _tmpTimeStart = _cursor.getLong(_cursorIndexOfTimeStart);
            }
            final int _tmpMaxHoursPerDay;
            _tmpMaxHoursPerDay = _cursor.getInt(_cursorIndexOfMaxHoursPerDay);
            final int _tmpNumberPlayers;
            _tmpNumberPlayers = _cursor.getInt(_cursorIndexOfNumberPlayers);
            final int _tmpNumberCourts;
            _tmpNumberCourts = _cursor.getInt(_cursorIndexOfNumberCourts);
            final boolean _tmpIsStarted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsStarted);
            _tmpIsStarted = _tmp_1 != 0;
            final boolean _tmpIsFinished;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFinished);
            _tmpIsFinished = _tmp_2 != 0;
            _result = new Tournament(_tmpId,_tmpName,_tmpType,_tmpScoreType,_tmpNumSets,_tmpMatchDuration,_tmpIsMixed,_tmpDateTour,_tmpTimeStart,_tmpMaxHoursPerDay,_tmpNumberPlayers,_tmpNumberCourts,_tmpIsStarted,_tmpIsFinished);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPlayersInTournament(final int tournamentId,
      final Continuation<? super List<Integer>> $completion) {
    final String _sql = "SELECT playerId FROM tournament_players WHERE tournamentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tournamentId);
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
