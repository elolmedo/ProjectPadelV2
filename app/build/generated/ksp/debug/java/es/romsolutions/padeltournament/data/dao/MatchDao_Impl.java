package es.romsolutions.padeltournament.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import es.romsolutions.padeltournament.data.model.Match;
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
public final class MatchDao_Impl implements MatchDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Match> __insertionAdapterOfMatch;

  private final EntityDeletionOrUpdateAdapter<Match> __deletionAdapterOfMatch;

  private final EntityDeletionOrUpdateAdapter<Match> __updateAdapterOfMatch;

  public MatchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMatch = new EntityInsertionAdapter<Match>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `matches` (`id`,`tournamentId`,`leagueId`,`player1Id`,`player2Id`,`player3Id`,`player4Id`,`teamOneId`,`teamTwoId`,`isByTime`,`playStart`,`playFinish`,`scoreTeamOne`,`scoreTeamTwo`,`courtNumber`,`weekNumber`,`roundNumber`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Match entity) {
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
        statement.bindLong(4, entity.getPlayer1Id());
        statement.bindLong(5, entity.getPlayer2Id());
        statement.bindLong(6, entity.getPlayer3Id());
        statement.bindLong(7, entity.getPlayer4Id());
        statement.bindLong(8, entity.getTeamOneId());
        statement.bindLong(9, entity.getTeamTwoId());
        final int _tmp = entity.isByTime() ? 1 : 0;
        statement.bindLong(10, _tmp);
        if (entity.getPlayStart() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getPlayStart());
        }
        if (entity.getPlayFinish() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getPlayFinish());
        }
        statement.bindLong(13, entity.getScoreTeamOne());
        statement.bindLong(14, entity.getScoreTeamTwo());
        statement.bindLong(15, entity.getCourtNumber());
        statement.bindLong(16, entity.getWeekNumber());
        statement.bindLong(17, entity.getRoundNumber());
      }
    };
    this.__deletionAdapterOfMatch = new EntityDeletionOrUpdateAdapter<Match>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `matches` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Match entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMatch = new EntityDeletionOrUpdateAdapter<Match>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `matches` SET `id` = ?,`tournamentId` = ?,`leagueId` = ?,`player1Id` = ?,`player2Id` = ?,`player3Id` = ?,`player4Id` = ?,`teamOneId` = ?,`teamTwoId` = ?,`isByTime` = ?,`playStart` = ?,`playFinish` = ?,`scoreTeamOne` = ?,`scoreTeamTwo` = ?,`courtNumber` = ?,`weekNumber` = ?,`roundNumber` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Match entity) {
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
        statement.bindLong(4, entity.getPlayer1Id());
        statement.bindLong(5, entity.getPlayer2Id());
        statement.bindLong(6, entity.getPlayer3Id());
        statement.bindLong(7, entity.getPlayer4Id());
        statement.bindLong(8, entity.getTeamOneId());
        statement.bindLong(9, entity.getTeamTwoId());
        final int _tmp = entity.isByTime() ? 1 : 0;
        statement.bindLong(10, _tmp);
        if (entity.getPlayStart() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getPlayStart());
        }
        if (entity.getPlayFinish() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getPlayFinish());
        }
        statement.bindLong(13, entity.getScoreTeamOne());
        statement.bindLong(14, entity.getScoreTeamTwo());
        statement.bindLong(15, entity.getCourtNumber());
        statement.bindLong(16, entity.getWeekNumber());
        statement.bindLong(17, entity.getRoundNumber());
        statement.bindLong(18, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Match match, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMatch.insert(match);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Match> matches, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMatch.insert(matches);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Match match, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMatch.handle(match);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Match match, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMatch.handle(match);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Match>> getMatchesByTournament(final int tournamentId) {
    final String _sql = "SELECT * FROM matches WHERE tournamentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tournamentId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<Match>>() {
      @Override
      @NonNull
      public List<Match> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfPlayer1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player1Id");
          final int _cursorIndexOfPlayer2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player2Id");
          final int _cursorIndexOfPlayer3Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player3Id");
          final int _cursorIndexOfPlayer4Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player4Id");
          final int _cursorIndexOfTeamOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamOneId");
          final int _cursorIndexOfTeamTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamTwoId");
          final int _cursorIndexOfIsByTime = CursorUtil.getColumnIndexOrThrow(_cursor, "isByTime");
          final int _cursorIndexOfPlayStart = CursorUtil.getColumnIndexOrThrow(_cursor, "playStart");
          final int _cursorIndexOfPlayFinish = CursorUtil.getColumnIndexOrThrow(_cursor, "playFinish");
          final int _cursorIndexOfScoreTeamOne = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamOne");
          final int _cursorIndexOfScoreTeamTwo = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamTwo");
          final int _cursorIndexOfCourtNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "courtNumber");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfRoundNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "roundNumber");
          final List<Match> _result = new ArrayList<Match>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Match _item;
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
            final int _tmpPlayer1Id;
            _tmpPlayer1Id = _cursor.getInt(_cursorIndexOfPlayer1Id);
            final int _tmpPlayer2Id;
            _tmpPlayer2Id = _cursor.getInt(_cursorIndexOfPlayer2Id);
            final int _tmpPlayer3Id;
            _tmpPlayer3Id = _cursor.getInt(_cursorIndexOfPlayer3Id);
            final int _tmpPlayer4Id;
            _tmpPlayer4Id = _cursor.getInt(_cursorIndexOfPlayer4Id);
            final int _tmpTeamOneId;
            _tmpTeamOneId = _cursor.getInt(_cursorIndexOfTeamOneId);
            final int _tmpTeamTwoId;
            _tmpTeamTwoId = _cursor.getInt(_cursorIndexOfTeamTwoId);
            final boolean _tmpIsByTime;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsByTime);
            _tmpIsByTime = _tmp != 0;
            final Long _tmpPlayStart;
            if (_cursor.isNull(_cursorIndexOfPlayStart)) {
              _tmpPlayStart = null;
            } else {
              _tmpPlayStart = _cursor.getLong(_cursorIndexOfPlayStart);
            }
            final Long _tmpPlayFinish;
            if (_cursor.isNull(_cursorIndexOfPlayFinish)) {
              _tmpPlayFinish = null;
            } else {
              _tmpPlayFinish = _cursor.getLong(_cursorIndexOfPlayFinish);
            }
            final int _tmpScoreTeamOne;
            _tmpScoreTeamOne = _cursor.getInt(_cursorIndexOfScoreTeamOne);
            final int _tmpScoreTeamTwo;
            _tmpScoreTeamTwo = _cursor.getInt(_cursorIndexOfScoreTeamTwo);
            final int _tmpCourtNumber;
            _tmpCourtNumber = _cursor.getInt(_cursorIndexOfCourtNumber);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final int _tmpRoundNumber;
            _tmpRoundNumber = _cursor.getInt(_cursorIndexOfRoundNumber);
            _item = new Match(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpPlayer1Id,_tmpPlayer2Id,_tmpPlayer3Id,_tmpPlayer4Id,_tmpTeamOneId,_tmpTeamTwoId,_tmpIsByTime,_tmpPlayStart,_tmpPlayFinish,_tmpScoreTeamOne,_tmpScoreTeamTwo,_tmpCourtNumber,_tmpWeekNumber,_tmpRoundNumber);
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
  public Flow<List<Match>> getMatchesByLeague(final int leagueId) {
    final String _sql = "SELECT * FROM matches WHERE leagueId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, leagueId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<Match>>() {
      @Override
      @NonNull
      public List<Match> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfPlayer1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player1Id");
          final int _cursorIndexOfPlayer2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player2Id");
          final int _cursorIndexOfPlayer3Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player3Id");
          final int _cursorIndexOfPlayer4Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player4Id");
          final int _cursorIndexOfTeamOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamOneId");
          final int _cursorIndexOfTeamTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamTwoId");
          final int _cursorIndexOfIsByTime = CursorUtil.getColumnIndexOrThrow(_cursor, "isByTime");
          final int _cursorIndexOfPlayStart = CursorUtil.getColumnIndexOrThrow(_cursor, "playStart");
          final int _cursorIndexOfPlayFinish = CursorUtil.getColumnIndexOrThrow(_cursor, "playFinish");
          final int _cursorIndexOfScoreTeamOne = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamOne");
          final int _cursorIndexOfScoreTeamTwo = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamTwo");
          final int _cursorIndexOfCourtNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "courtNumber");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfRoundNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "roundNumber");
          final List<Match> _result = new ArrayList<Match>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Match _item;
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
            final int _tmpPlayer1Id;
            _tmpPlayer1Id = _cursor.getInt(_cursorIndexOfPlayer1Id);
            final int _tmpPlayer2Id;
            _tmpPlayer2Id = _cursor.getInt(_cursorIndexOfPlayer2Id);
            final int _tmpPlayer3Id;
            _tmpPlayer3Id = _cursor.getInt(_cursorIndexOfPlayer3Id);
            final int _tmpPlayer4Id;
            _tmpPlayer4Id = _cursor.getInt(_cursorIndexOfPlayer4Id);
            final int _tmpTeamOneId;
            _tmpTeamOneId = _cursor.getInt(_cursorIndexOfTeamOneId);
            final int _tmpTeamTwoId;
            _tmpTeamTwoId = _cursor.getInt(_cursorIndexOfTeamTwoId);
            final boolean _tmpIsByTime;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsByTime);
            _tmpIsByTime = _tmp != 0;
            final Long _tmpPlayStart;
            if (_cursor.isNull(_cursorIndexOfPlayStart)) {
              _tmpPlayStart = null;
            } else {
              _tmpPlayStart = _cursor.getLong(_cursorIndexOfPlayStart);
            }
            final Long _tmpPlayFinish;
            if (_cursor.isNull(_cursorIndexOfPlayFinish)) {
              _tmpPlayFinish = null;
            } else {
              _tmpPlayFinish = _cursor.getLong(_cursorIndexOfPlayFinish);
            }
            final int _tmpScoreTeamOne;
            _tmpScoreTeamOne = _cursor.getInt(_cursorIndexOfScoreTeamOne);
            final int _tmpScoreTeamTwo;
            _tmpScoreTeamTwo = _cursor.getInt(_cursorIndexOfScoreTeamTwo);
            final int _tmpCourtNumber;
            _tmpCourtNumber = _cursor.getInt(_cursorIndexOfCourtNumber);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final int _tmpRoundNumber;
            _tmpRoundNumber = _cursor.getInt(_cursorIndexOfRoundNumber);
            _item = new Match(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpPlayer1Id,_tmpPlayer2Id,_tmpPlayer3Id,_tmpPlayer4Id,_tmpTeamOneId,_tmpTeamTwoId,_tmpIsByTime,_tmpPlayStart,_tmpPlayFinish,_tmpScoreTeamOne,_tmpScoreTeamTwo,_tmpCourtNumber,_tmpWeekNumber,_tmpRoundNumber);
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
  public Flow<List<Match>> getAllMatches() {
    final String _sql = "SELECT * FROM matches ORDER BY playStart ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<Match>>() {
      @Override
      @NonNull
      public List<Match> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfPlayer1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player1Id");
          final int _cursorIndexOfPlayer2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player2Id");
          final int _cursorIndexOfPlayer3Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player3Id");
          final int _cursorIndexOfPlayer4Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player4Id");
          final int _cursorIndexOfTeamOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamOneId");
          final int _cursorIndexOfTeamTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamTwoId");
          final int _cursorIndexOfIsByTime = CursorUtil.getColumnIndexOrThrow(_cursor, "isByTime");
          final int _cursorIndexOfPlayStart = CursorUtil.getColumnIndexOrThrow(_cursor, "playStart");
          final int _cursorIndexOfPlayFinish = CursorUtil.getColumnIndexOrThrow(_cursor, "playFinish");
          final int _cursorIndexOfScoreTeamOne = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamOne");
          final int _cursorIndexOfScoreTeamTwo = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamTwo");
          final int _cursorIndexOfCourtNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "courtNumber");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfRoundNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "roundNumber");
          final List<Match> _result = new ArrayList<Match>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Match _item;
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
            final int _tmpPlayer1Id;
            _tmpPlayer1Id = _cursor.getInt(_cursorIndexOfPlayer1Id);
            final int _tmpPlayer2Id;
            _tmpPlayer2Id = _cursor.getInt(_cursorIndexOfPlayer2Id);
            final int _tmpPlayer3Id;
            _tmpPlayer3Id = _cursor.getInt(_cursorIndexOfPlayer3Id);
            final int _tmpPlayer4Id;
            _tmpPlayer4Id = _cursor.getInt(_cursorIndexOfPlayer4Id);
            final int _tmpTeamOneId;
            _tmpTeamOneId = _cursor.getInt(_cursorIndexOfTeamOneId);
            final int _tmpTeamTwoId;
            _tmpTeamTwoId = _cursor.getInt(_cursorIndexOfTeamTwoId);
            final boolean _tmpIsByTime;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsByTime);
            _tmpIsByTime = _tmp != 0;
            final Long _tmpPlayStart;
            if (_cursor.isNull(_cursorIndexOfPlayStart)) {
              _tmpPlayStart = null;
            } else {
              _tmpPlayStart = _cursor.getLong(_cursorIndexOfPlayStart);
            }
            final Long _tmpPlayFinish;
            if (_cursor.isNull(_cursorIndexOfPlayFinish)) {
              _tmpPlayFinish = null;
            } else {
              _tmpPlayFinish = _cursor.getLong(_cursorIndexOfPlayFinish);
            }
            final int _tmpScoreTeamOne;
            _tmpScoreTeamOne = _cursor.getInt(_cursorIndexOfScoreTeamOne);
            final int _tmpScoreTeamTwo;
            _tmpScoreTeamTwo = _cursor.getInt(_cursorIndexOfScoreTeamTwo);
            final int _tmpCourtNumber;
            _tmpCourtNumber = _cursor.getInt(_cursorIndexOfCourtNumber);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final int _tmpRoundNumber;
            _tmpRoundNumber = _cursor.getInt(_cursorIndexOfRoundNumber);
            _item = new Match(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpPlayer1Id,_tmpPlayer2Id,_tmpPlayer3Id,_tmpPlayer4Id,_tmpTeamOneId,_tmpTeamTwoId,_tmpIsByTime,_tmpPlayStart,_tmpPlayFinish,_tmpScoreTeamOne,_tmpScoreTeamTwo,_tmpCourtNumber,_tmpWeekNumber,_tmpRoundNumber);
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
  public Object getMatchesByTournamentSync(final int tournamentId,
      final Continuation<? super List<Match>> $completion) {
    final String _sql = "SELECT * FROM matches WHERE tournamentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tournamentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Match>>() {
      @Override
      @NonNull
      public List<Match> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfPlayer1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player1Id");
          final int _cursorIndexOfPlayer2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player2Id");
          final int _cursorIndexOfPlayer3Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player3Id");
          final int _cursorIndexOfPlayer4Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player4Id");
          final int _cursorIndexOfTeamOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamOneId");
          final int _cursorIndexOfTeamTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamTwoId");
          final int _cursorIndexOfIsByTime = CursorUtil.getColumnIndexOrThrow(_cursor, "isByTime");
          final int _cursorIndexOfPlayStart = CursorUtil.getColumnIndexOrThrow(_cursor, "playStart");
          final int _cursorIndexOfPlayFinish = CursorUtil.getColumnIndexOrThrow(_cursor, "playFinish");
          final int _cursorIndexOfScoreTeamOne = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamOne");
          final int _cursorIndexOfScoreTeamTwo = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamTwo");
          final int _cursorIndexOfCourtNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "courtNumber");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfRoundNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "roundNumber");
          final List<Match> _result = new ArrayList<Match>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Match _item;
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
            final int _tmpPlayer1Id;
            _tmpPlayer1Id = _cursor.getInt(_cursorIndexOfPlayer1Id);
            final int _tmpPlayer2Id;
            _tmpPlayer2Id = _cursor.getInt(_cursorIndexOfPlayer2Id);
            final int _tmpPlayer3Id;
            _tmpPlayer3Id = _cursor.getInt(_cursorIndexOfPlayer3Id);
            final int _tmpPlayer4Id;
            _tmpPlayer4Id = _cursor.getInt(_cursorIndexOfPlayer4Id);
            final int _tmpTeamOneId;
            _tmpTeamOneId = _cursor.getInt(_cursorIndexOfTeamOneId);
            final int _tmpTeamTwoId;
            _tmpTeamTwoId = _cursor.getInt(_cursorIndexOfTeamTwoId);
            final boolean _tmpIsByTime;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsByTime);
            _tmpIsByTime = _tmp != 0;
            final Long _tmpPlayStart;
            if (_cursor.isNull(_cursorIndexOfPlayStart)) {
              _tmpPlayStart = null;
            } else {
              _tmpPlayStart = _cursor.getLong(_cursorIndexOfPlayStart);
            }
            final Long _tmpPlayFinish;
            if (_cursor.isNull(_cursorIndexOfPlayFinish)) {
              _tmpPlayFinish = null;
            } else {
              _tmpPlayFinish = _cursor.getLong(_cursorIndexOfPlayFinish);
            }
            final int _tmpScoreTeamOne;
            _tmpScoreTeamOne = _cursor.getInt(_cursorIndexOfScoreTeamOne);
            final int _tmpScoreTeamTwo;
            _tmpScoreTeamTwo = _cursor.getInt(_cursorIndexOfScoreTeamTwo);
            final int _tmpCourtNumber;
            _tmpCourtNumber = _cursor.getInt(_cursorIndexOfCourtNumber);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final int _tmpRoundNumber;
            _tmpRoundNumber = _cursor.getInt(_cursorIndexOfRoundNumber);
            _item = new Match(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpPlayer1Id,_tmpPlayer2Id,_tmpPlayer3Id,_tmpPlayer4Id,_tmpTeamOneId,_tmpTeamTwoId,_tmpIsByTime,_tmpPlayStart,_tmpPlayFinish,_tmpScoreTeamOne,_tmpScoreTeamTwo,_tmpCourtNumber,_tmpWeekNumber,_tmpRoundNumber);
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

  @Override
  public Object getMatchesByLeagueSync(final int leagueId,
      final Continuation<? super List<Match>> $completion) {
    final String _sql = "SELECT * FROM matches WHERE leagueId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, leagueId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Match>>() {
      @Override
      @NonNull
      public List<Match> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTournamentId = CursorUtil.getColumnIndexOrThrow(_cursor, "tournamentId");
          final int _cursorIndexOfLeagueId = CursorUtil.getColumnIndexOrThrow(_cursor, "leagueId");
          final int _cursorIndexOfPlayer1Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player1Id");
          final int _cursorIndexOfPlayer2Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player2Id");
          final int _cursorIndexOfPlayer3Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player3Id");
          final int _cursorIndexOfPlayer4Id = CursorUtil.getColumnIndexOrThrow(_cursor, "player4Id");
          final int _cursorIndexOfTeamOneId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamOneId");
          final int _cursorIndexOfTeamTwoId = CursorUtil.getColumnIndexOrThrow(_cursor, "teamTwoId");
          final int _cursorIndexOfIsByTime = CursorUtil.getColumnIndexOrThrow(_cursor, "isByTime");
          final int _cursorIndexOfPlayStart = CursorUtil.getColumnIndexOrThrow(_cursor, "playStart");
          final int _cursorIndexOfPlayFinish = CursorUtil.getColumnIndexOrThrow(_cursor, "playFinish");
          final int _cursorIndexOfScoreTeamOne = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamOne");
          final int _cursorIndexOfScoreTeamTwo = CursorUtil.getColumnIndexOrThrow(_cursor, "scoreTeamTwo");
          final int _cursorIndexOfCourtNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "courtNumber");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfRoundNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "roundNumber");
          final List<Match> _result = new ArrayList<Match>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Match _item;
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
            final int _tmpPlayer1Id;
            _tmpPlayer1Id = _cursor.getInt(_cursorIndexOfPlayer1Id);
            final int _tmpPlayer2Id;
            _tmpPlayer2Id = _cursor.getInt(_cursorIndexOfPlayer2Id);
            final int _tmpPlayer3Id;
            _tmpPlayer3Id = _cursor.getInt(_cursorIndexOfPlayer3Id);
            final int _tmpPlayer4Id;
            _tmpPlayer4Id = _cursor.getInt(_cursorIndexOfPlayer4Id);
            final int _tmpTeamOneId;
            _tmpTeamOneId = _cursor.getInt(_cursorIndexOfTeamOneId);
            final int _tmpTeamTwoId;
            _tmpTeamTwoId = _cursor.getInt(_cursorIndexOfTeamTwoId);
            final boolean _tmpIsByTime;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsByTime);
            _tmpIsByTime = _tmp != 0;
            final Long _tmpPlayStart;
            if (_cursor.isNull(_cursorIndexOfPlayStart)) {
              _tmpPlayStart = null;
            } else {
              _tmpPlayStart = _cursor.getLong(_cursorIndexOfPlayStart);
            }
            final Long _tmpPlayFinish;
            if (_cursor.isNull(_cursorIndexOfPlayFinish)) {
              _tmpPlayFinish = null;
            } else {
              _tmpPlayFinish = _cursor.getLong(_cursorIndexOfPlayFinish);
            }
            final int _tmpScoreTeamOne;
            _tmpScoreTeamOne = _cursor.getInt(_cursorIndexOfScoreTeamOne);
            final int _tmpScoreTeamTwo;
            _tmpScoreTeamTwo = _cursor.getInt(_cursorIndexOfScoreTeamTwo);
            final int _tmpCourtNumber;
            _tmpCourtNumber = _cursor.getInt(_cursorIndexOfCourtNumber);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final int _tmpRoundNumber;
            _tmpRoundNumber = _cursor.getInt(_cursorIndexOfRoundNumber);
            _item = new Match(_tmpId,_tmpTournamentId,_tmpLeagueId,_tmpPlayer1Id,_tmpPlayer2Id,_tmpPlayer3Id,_tmpPlayer4Id,_tmpTeamOneId,_tmpTeamTwoId,_tmpIsByTime,_tmpPlayStart,_tmpPlayFinish,_tmpScoreTeamOne,_tmpScoreTeamTwo,_tmpCourtNumber,_tmpWeekNumber,_tmpRoundNumber);
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
