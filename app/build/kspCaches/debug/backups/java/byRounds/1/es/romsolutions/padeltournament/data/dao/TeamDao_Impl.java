package es.romsolutions.padeltournament.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import es.romsolutions.padeltournament.data.model.Team;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TeamDao_Impl implements TeamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Team> __insertionAdapterOfTeam;

  public TeamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeam = new EntityInsertionAdapter<Team>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `teams` (`id`,`tournamentId`,`leagueId`,`nameTeam`,`playerOneId`,`playerTwoId`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Team entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTournamentId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getTournamentId());
        }
        if (entity.getLeagueId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getLeagueId());
        }
        statement.bindString(4, entity.getNameTeam());
        statement.bindLong(5, entity.getPlayerOneId());
        statement.bindLong(6, entity.getPlayerTwoId());
      }
    };
  }

  @Override
  public Object insert(final Team team, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTeam.insertAndReturnId(team);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Team> teams,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfTeam.insertAndReturnIdsList(teams);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Team>> getTeamsByTournament(final int tournamentId) {
    final String _sql = "SELECT * FROM teams WHERE tournamentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tournamentId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<List<Team>>() {
      @Override
      @NonNull
      public List<Team> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfNameTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "nameTeam");
          final int _cursorIndexOfPlayerOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerOneId");
          final int _cursorIndexOfPlayerTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerTwoId");
          final List<Team> _result = new ArrayList<Team>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Team _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpTournamentId;
            if (_cursor.isNull(_cursorIndexOfTournamentId)) {
              _tmpTournamentId = null;
            } else {
              _tmpTournamentId = _cursor.getInt(_cursorIndexOfTournamentId);
            }
            final Integer _tmpLeagueId;
            if (_cursor.isNull(_cursorIndexOfLeagueId)) {
              _tmpLeagueId = null;
            } else {
              _tmpLeagueId = _cursor.getInt(_cursorIndexOfLeagueId);
            }
            final String _tmpNameTeam;
            _tmpNameTeam = _cursor.getString(_cursorIndexOfNameTeam);
            final int _tmpPlayerOneId;
            _tmpPlayerOneId = _cursor.getInt(_cursorIndexOfPlayerOneId);
            final int _tmpPlayerTwoId;
            _tmpPlayerTwoId = _cursor.getInt(_cursorIndexOfPlayerTwoId);
            _item = new Team(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpNameTeam,_tmpPlayerOneId,_tmpPlayerTwoId);
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
  public Flow<List<Team>> getTeamsByLeague(final int leagueId) {
    final String _sql = "SELECT * FROM teams WHERE leagueId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, leagueId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<List<Team>>() {
      @Override
      @NonNull
      public List<Team> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfNameTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "nameTeam");
          final int _cursorIndexOfPlayerOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerOneId");
          final int _cursorIndexOfPlayerTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerTwoId");
          final List<Team> _result = new ArrayList<Team>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Team _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpTournamentId;
            if (_cursor.isNull(_cursorIndexOfTournamentId)) {
              _tmpTournamentId = null;
            } else {
              _tmpTournamentId = _cursor.getInt(_cursorIndexOfTournamentId);
            }
            final Integer _tmpLeagueId;
            if (_cursor.isNull(_cursorIndexOfLeagueId)) {
              _tmpLeagueId = null;
            } else {
              _tmpLeagueId = _cursor.getInt(_cursorIndexOfLeagueId);
            }
            final String _tmpNameTeam;
            _tmpNameTeam = _cursor.getString(_cursorIndexOfNameTeam);
            final int _tmpPlayerOneId;
            _tmpPlayerOneId = _cursor.getInt(_cursorIndexOfPlayerOneId);
            final int _tmpPlayerTwoId;
            _tmpPlayerTwoId = _cursor.getInt(_cursorIndexOfPlayerTwoId);
            _item = new Team(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpNameTeam,_tmpPlayerOneId,_tmpPlayerTwoId);
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
  public Flow<List<Team>> getAllTeams() {
    final String _sql = "SELECT * FROM teams";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"teams"}, new Callable<List<Team>>() {
      @Override
      @NonNull
      public List<Team> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfNameTeam = CursorUtil.getColumnIndexOrThrow(_cursor, "nameTeam");
          final int _cursorIndexOfPlayerOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerOneId");
          final int _cursorIndexOfPlayerTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerTwoId");
          final List<Team> _result = new ArrayList<Team>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Team _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Integer _tmpTournamentId;
            if (_cursor.isNull(_cursorIndexOfTournamentId)) {
              _tmpTournamentId = null;
            } else {
              _tmpTournamentId = _cursor.getInt(_cursorIndexOfTournamentId);
            }
            final Integer _tmpLeagueId;
            if (_cursor.isNull(_cursorIndexOfLeagueId)) {
              _tmpLeagueId = null;
            } else {
              _tmpLeagueId = _cursor.getInt(_cursorIndexOfLeagueId);
            }
            final String _tmpNameTeam;
            _tmpNameTeam = _cursor.getString(_cursorIndexOfNameTeam);
            final int _tmpPlayerOneId;
            _tmpPlayerOneId = _cursor.getInt(_cursorIndexOfPlayerOneId);
            final int _tmpPlayerTwoId;
            _tmpPlayerTwoId = _cursor.getInt(_cursorIndexOfPlayerTwoId);
            _item = new Team(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpNameTeam,_tmpPlayerOneId,_tmpPlayerTwoId);
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
