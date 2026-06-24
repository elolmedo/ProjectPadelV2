package es.romsolutions.padeltournament.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import es.romsolutions.padeltournament.data.model.Player;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class PlayerDao_Impl implements PlayerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Player> __insertionAdapterOfPlayer;

  private final SharedSQLiteStatement __preparedStmtOfDeletePlayerById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllPlayers;

  public PlayerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlayer = new EntityInsertionAdapter<Player>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `players` (`idplayer`,`name`,`sex`,`mail`,`phone`,`numbertorneosparticipate`,`wintournaments`,`setplayed`,`setwinner`,`adminid`,`level`,`photo_uri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Player entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getNombre());
        statement.bindString(3, entity.getSexo());
        statement.bindString(4, entity.getEmail());
        statement.bindString(5, entity.getPhone());
        statement.bindLong(6, entity.getTournamentsPlayed());
        statement.bindLong(7, entity.getTournamentsWon());
        statement.bindLong(8, entity.getSetsPlayed());
        statement.bindLong(9, entity.getSetsWon());
        if (entity.getAdminId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getAdminId());
        }
        statement.bindDouble(11, entity.getLevel());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPhotoUri());
        }
      }
    };
    this.__preparedStmtOfDeletePlayerById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM players WHERE idplayer = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllPlayers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM players";
        return _query;
      }
    };
  }

  @Override
  public Object insertPlayer(final Player player, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlayer.insert(player);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlayerById(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePlayerById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeletePlayerById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllPlayers(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllPlayers.acquire();
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
          __preparedStmtOfDeleteAllPlayers.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Player>> getPlayersByAdmin(final String adminId) {
    final String _sql = "SELECT * FROM players WHERE adminid = ? OR adminid IS NULL ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (adminId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, adminId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"players"}, new Callable<List<Player>>() {
      @Override
      @NonNull
      public List<Player> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "idplayer");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSexo = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "mail");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfTournamentsPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "numbertorneosparticipate");
          final int _cursorIndexOfTournamentsWon = CursorUtil.getColumnIndexOrThrow(_cursor, "wintournaments");
          final int _cursorIndexOfSetsPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "setplayed");
          final int _cursorIndexOfSetsWon = CursorUtil.getColumnIndexOrThrow(_cursor, "setwinner");
          final int _cursorIndexOfAdminId = CursorUtil.getColumnIndexOrThrow(_cursor, "adminid");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photo_uri");
          final List<Player> _result = new ArrayList<Player>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Player _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpSexo;
            _tmpSexo = _cursor.getString(_cursorIndexOfSexo);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final int _tmpTournamentsPlayed;
            _tmpTournamentsPlayed = _cursor.getInt(_cursorIndexOfTournamentsPlayed);
            final int _tmpTournamentsWon;
            _tmpTournamentsWon = _cursor.getInt(_cursorIndexOfTournamentsWon);
            final int _tmpSetsPlayed;
            _tmpSetsPlayed = _cursor.getInt(_cursorIndexOfSetsPlayed);
            final int _tmpSetsWon;
            _tmpSetsWon = _cursor.getInt(_cursorIndexOfSetsWon);
            final String _tmpAdminId;
            if (_cursor.isNull(_cursorIndexOfAdminId)) {
              _tmpAdminId = null;
            } else {
              _tmpAdminId = _cursor.getString(_cursorIndexOfAdminId);
            }
            final double _tmpLevel;
            _tmpLevel = _cursor.getDouble(_cursorIndexOfLevel);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item = new Player(_tmpId,_tmpNombre,_tmpSexo,_tmpEmail,_tmpPhone,_tmpTournamentsPlayed,_tmpTournamentsWon,_tmpSetsPlayed,_tmpSetsWon,_tmpAdminId,_tmpLevel,_tmpPhotoUri);
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
  public Object getPlayerCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM players";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Object getPlayersByIds(final List<Integer> ids,
      final Continuation<? super List<Player>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM players WHERE idplayer IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Player>>() {
      @Override
      @NonNull
      public List<Player> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "idplayer");
          final int _cursorIndexOfNombre = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSexo = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "mail");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfTournamentsPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "numbertorneosparticipate");
          final int _cursorIndexOfTournamentsWon = CursorUtil.getColumnIndexOrThrow(_cursor, "wintournaments");
          final int _cursorIndexOfSetsPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "setplayed");
          final int _cursorIndexOfSetsWon = CursorUtil.getColumnIndexOrThrow(_cursor, "setwinner");
          final int _cursorIndexOfAdminId = CursorUtil.getColumnIndexOrThrow(_cursor, "adminid");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photo_uri");
          final List<Player> _result = new ArrayList<Player>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Player _item_1;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNombre;
            _tmpNombre = _cursor.getString(_cursorIndexOfNombre);
            final String _tmpSexo;
            _tmpSexo = _cursor.getString(_cursorIndexOfSexo);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpPhone;
            _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            final int _tmpTournamentsPlayed;
            _tmpTournamentsPlayed = _cursor.getInt(_cursorIndexOfTournamentsPlayed);
            final int _tmpTournamentsWon;
            _tmpTournamentsWon = _cursor.getInt(_cursorIndexOfTournamentsWon);
            final int _tmpSetsPlayed;
            _tmpSetsPlayed = _cursor.getInt(_cursorIndexOfSetsPlayed);
            final int _tmpSetsWon;
            _tmpSetsWon = _cursor.getInt(_cursorIndexOfSetsWon);
            final String _tmpAdminId;
            if (_cursor.isNull(_cursorIndexOfAdminId)) {
              _tmpAdminId = null;
            } else {
              _tmpAdminId = _cursor.getString(_cursorIndexOfAdminId);
            }
            final double _tmpLevel;
            _tmpLevel = _cursor.getDouble(_cursorIndexOfLevel);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item_1 = new Player(_tmpId,_tmpNombre,_tmpSexo,_tmpEmail,_tmpPhone,_tmpTournamentsPlayed,_tmpTournamentsWon,_tmpSetsPlayed,_tmpSetsWon,_tmpAdminId,_tmpLevel,_tmpPhotoUri);
            _result.add(_item_1);
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
