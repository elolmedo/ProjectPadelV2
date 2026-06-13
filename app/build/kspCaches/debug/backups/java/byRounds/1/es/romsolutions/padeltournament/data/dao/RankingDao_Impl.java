package es.romsolutions.padeltournament.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import es.romsolutions.padeltournament.data.model.Ranking;
import java.lang.Class;
import java.lang.Exception;
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
public final class RankingDao_Impl implements RankingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Ranking> __insertionAdapterOfRanking;

  private final EntityDeletionOrUpdateAdapter<Ranking> __updateAdapterOfRanking;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RankingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRanking = new EntityInsertionAdapter<Ranking>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rankings` (`leagueId`,`playerId`,`points`,`matchesPlayed`,`matchesWon`,`matchesLost`,`position`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Ranking entity) {
        statement.bindLong(1, entity.getLeagueId());
        statement.bindLong(2, entity.getPlayerId());
        statement.bindLong(3, entity.getPoints());
        statement.bindLong(4, entity.getMatchesPlayed());
        statement.bindLong(5, entity.getMatchesWon());
        statement.bindLong(6, entity.getMatchesLost());
        statement.bindLong(7, entity.getPosition());
      }
    };
    this.__updateAdapterOfRanking = new EntityDeletionOrUpdateAdapter<Ranking>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rankings` SET `leagueId` = ?,`playerId` = ?,`points` = ?,`matchesPlayed` = ?,`matchesWon` = ?,`matchesLost` = ?,`position` = ? WHERE `leagueId` = ? AND `playerId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Ranking entity) {
        statement.bindLong(1, entity.getLeagueId());
        statement.bindLong(2, entity.getPlayerId());
        statement.bindLong(3, entity.getPoints());
        statement.bindLong(4, entity.getMatchesPlayed());
        statement.bindLong(5, entity.getMatchesWon());
        statement.bindLong(6, entity.getMatchesLost());
        statement.bindLong(7, entity.getPosition());
        statement.bindLong(8, entity.getLeagueId());
        statement.bindLong(9, entity.getPlayerId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rankings";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Ranking ranking, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRanking.insert(ranking);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Ranking> rankings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRanking.insert(rankings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Ranking ranking, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRanking.handle(ranking);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Ranking>> getRankingForLeague(final int leagueId) {
    final String _sql = "SELECT * FROM rankings WHERE leagueId = ? ORDER BY points DESC, matchesWon DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, leagueId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rankings"}, new Callable<List<Ranking>>() {
      @Override
      @NonNull
      public List<Ranking> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfPlayerId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerId");
          final int _cursorIndexOfPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "points");
          final int _cursorIndexOfMatchesPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "matchesPlayed");
          final int _cursorIndexOfMatchesWon = CursorUtil.getColumnIndexOrThrow(_cursor, "matchesWon");
          final int _cursorIndexOfMatchesLost = CursorUtil.getColumnIndexOrThrow(_cursor, "matchesLost");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final List<Ranking> _result = new ArrayList<Ranking>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Ranking _item;
            final int _tmpLeagueId;
            _tmpLeagueId = _cursor.getInt(_cursorIndexOfLeagueId);
            final int _tmpPlayerId;
            _tmpPlayerId = _cursor.getInt(_cursorIndexOfPlayerId);
            final int _tmpPoints;
            _tmpPoints = _cursor.getInt(_cursorIndexOfPoints);
            final int _tmpMatchesPlayed;
            _tmpMatchesPlayed = _cursor.getInt(_cursorIndexOfMatchesPlayed);
            final int _tmpMatchesWon;
            _tmpMatchesWon = _cursor.getInt(_cursorIndexOfMatchesWon);
            final int _tmpMatchesLost;
            _tmpMatchesLost = _cursor.getInt(_cursorIndexOfMatchesLost);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _item = new Ranking(_tmpLeagueId,_tmpPlayerId,_tmpPoints,_tmpMatchesPlayed,_tmpMatchesWon,_tmpMatchesLost,_tmpPosition);
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
  public Flow<List<Ranking>> getGeneralRanking() {
    final String _sql = "SELECT playerId, SUM(points) as points, SUM(matchesPlayed) as matchesPlayed, SUM(matchesWon) as matchesWon, SUM(matchesLost) as matchesLost, 0 as leagueId, 0 as position FROM rankings GROUP BY playerId ORDER BY points DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rankings"}, new Callable<List<Ranking>>() {
      @Override
      @NonNull
      public List<Ranking> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayerId = 0;
          final int _cursorIndexOfPoints = 1;
          final int _cursorIndexOfMatchesPlayed = 2;
          final int _cursorIndexOfMatchesWon = 3;
          final int _cursorIndexOfMatchesLost = 4;
          final int _cursorIndexOfLeagueId = 5;
          final int _cursorIndexOfPosition = 6;
          final List<Ranking> _result = new ArrayList<Ranking>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Ranking _item;
            final int _tmpPlayerId;
            _tmpPlayerId = _cursor.getInt(_cursorIndexOfPlayerId);
            final int _tmpPoints;
            _tmpPoints = _cursor.getInt(_cursorIndexOfPoints);
            final int _tmpMatchesPlayed;
            _tmpMatchesPlayed = _cursor.getInt(_cursorIndexOfMatchesPlayed);
            final int _tmpMatchesWon;
            _tmpMatchesWon = _cursor.getInt(_cursorIndexOfMatchesWon);
            final int _tmpMatchesLost;
            _tmpMatchesLost = _cursor.getInt(_cursorIndexOfMatchesLost);
            final int _tmpLeagueId;
            _tmpLeagueId = _cursor.getInt(_cursorIndexOfLeagueId);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _item = new Ranking(_tmpLeagueId,_tmpPlayerId,_tmpPoints,_tmpMatchesPlayed,_tmpMatchesWon,_tmpMatchesLost,_tmpPosition);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
