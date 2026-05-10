package com.eskisehir.events.data.local.database;

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
import com.eskisehir.events.data.local.dao.CommentDao;
import com.eskisehir.events.data.local.dao.CommentDao_Impl;
import com.eskisehir.events.data.local.dao.FavoriteDao;
import com.eskisehir.events.data.local.dao.FavoriteDao_Impl;
import com.eskisehir.events.data.local.dao.FavoritePlaceDao;
import com.eskisehir.events.data.local.dao.FavoritePlaceDao_Impl;
import com.eskisehir.events.data.local.dao.RoadmapStopDao;
import com.eskisehir.events.data.local.dao.RoadmapStopDao_Impl;
import com.eskisehir.events.data.local.dao.UserDao;
import com.eskisehir.events.data.local.dao.UserDao_Impl;
import com.eskisehir.events.data.local.dao.UserEventStatusDao;
import com.eskisehir.events.data.local.dao.UserEventStatusDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile FavoriteDao _favoriteDao;

  private volatile UserDao _userDao;

  private volatile CommentDao _commentDao;

  private volatile UserEventStatusDao _userEventStatusDao;

  private volatile FavoritePlaceDao _favoritePlaceDao;

  private volatile RoadmapStopDao _roadmapStopDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`eventId` INTEGER NOT NULL, PRIMARY KEY(`eventId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`email` TEXT NOT NULL, `fullName` TEXT, `profileImageUri` TEXT, `interests` TEXT NOT NULL, PRIMARY KEY(`email`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `comments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventId` INTEGER NOT NULL, `userEmail` TEXT NOT NULL, `userName` TEXT NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_event_status` (`eventId` INTEGER NOT NULL, `status` TEXT NOT NULL, PRIMARY KEY(`eventId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_places` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `location` TEXT NOT NULL, `category` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `roadmap_stops` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventId` INTEGER NOT NULL, `title` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `locationName` TEXT NOT NULL, `address` TEXT NOT NULL, `date` TEXT NOT NULL, `stopOrder` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c4f81e67dec0512a557eae29ff4b932c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `favorites`");
        db.execSQL("DROP TABLE IF EXISTS `user_profile`");
        db.execSQL("DROP TABLE IF EXISTS `comments`");
        db.execSQL("DROP TABLE IF EXISTS `user_event_status`");
        db.execSQL("DROP TABLE IF EXISTS `favorite_places`");
        db.execSQL("DROP TABLE IF EXISTS `roadmap_stops`");
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
        final HashMap<String, TableInfo.Column> _columnsFavorites = new HashMap<String, TableInfo.Column>(1);
        _columnsFavorites.put("eventId", new TableInfo.Column("eventId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavorites = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavorites = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavorites = new TableInfo("favorites", _columnsFavorites, _foreignKeysFavorites, _indicesFavorites);
        final TableInfo _existingFavorites = TableInfo.read(db, "favorites");
        if (!_infoFavorites.equals(_existingFavorites)) {
          return new RoomOpenHelper.ValidationResult(false, "favorites(com.eskisehir.events.data.local.entity.FavoriteEntity).\n"
                  + " Expected:\n" + _infoFavorites + "\n"
                  + " Found:\n" + _existingFavorites);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProfile = new HashMap<String, TableInfo.Column>(4);
        _columnsUserProfile.put("email", new TableInfo.Column("email", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("fullName", new TableInfo.Column("fullName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("profileImageUri", new TableInfo.Column("profileImageUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("interests", new TableInfo.Column("interests", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfile = new TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile);
        final TableInfo _existingUserProfile = TableInfo.read(db, "user_profile");
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profile(com.eskisehir.events.data.local.entity.UserProfileEntity).\n"
                  + " Expected:\n" + _infoUserProfile + "\n"
                  + " Found:\n" + _existingUserProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsComments = new HashMap<String, TableInfo.Column>(6);
        _columnsComments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("eventId", new TableInfo.Column("eventId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("userEmail", new TableInfo.Column("userEmail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("userName", new TableInfo.Column("userName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysComments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesComments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoComments = new TableInfo("comments", _columnsComments, _foreignKeysComments, _indicesComments);
        final TableInfo _existingComments = TableInfo.read(db, "comments");
        if (!_infoComments.equals(_existingComments)) {
          return new RoomOpenHelper.ValidationResult(false, "comments(com.eskisehir.events.data.local.entity.CommentEntity).\n"
                  + " Expected:\n" + _infoComments + "\n"
                  + " Found:\n" + _existingComments);
        }
        final HashMap<String, TableInfo.Column> _columnsUserEventStatus = new HashMap<String, TableInfo.Column>(2);
        _columnsUserEventStatus.put("eventId", new TableInfo.Column("eventId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserEventStatus.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserEventStatus = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserEventStatus = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserEventStatus = new TableInfo("user_event_status", _columnsUserEventStatus, _foreignKeysUserEventStatus, _indicesUserEventStatus);
        final TableInfo _existingUserEventStatus = TableInfo.read(db, "user_event_status");
        if (!_infoUserEventStatus.equals(_existingUserEventStatus)) {
          return new RoomOpenHelper.ValidationResult(false, "user_event_status(com.eskisehir.events.data.local.entity.UserEventStatusEntity).\n"
                  + " Expected:\n" + _infoUserEventStatus + "\n"
                  + " Found:\n" + _existingUserEventStatus);
        }
        final HashMap<String, TableInfo.Column> _columnsFavoritePlaces = new HashMap<String, TableInfo.Column>(4);
        _columnsFavoritePlaces.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoritePlaces.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoritePlaces.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoritePlaces.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoritePlaces = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavoritePlaces = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavoritePlaces = new TableInfo("favorite_places", _columnsFavoritePlaces, _foreignKeysFavoritePlaces, _indicesFavoritePlaces);
        final TableInfo _existingFavoritePlaces = TableInfo.read(db, "favorite_places");
        if (!_infoFavoritePlaces.equals(_existingFavoritePlaces)) {
          return new RoomOpenHelper.ValidationResult(false, "favorite_places(com.eskisehir.events.data.local.entity.FavoritePlaceEntity).\n"
                  + " Expected:\n" + _infoFavoritePlaces + "\n"
                  + " Found:\n" + _existingFavoritePlaces);
        }
        final HashMap<String, TableInfo.Column> _columnsRoadmapStops = new HashMap<String, TableInfo.Column>(9);
        _columnsRoadmapStops.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("eventId", new TableInfo.Column("eventId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("locationName", new TableInfo.Column("locationName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRoadmapStops.put("stopOrder", new TableInfo.Column("stopOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRoadmapStops = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRoadmapStops = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRoadmapStops = new TableInfo("roadmap_stops", _columnsRoadmapStops, _foreignKeysRoadmapStops, _indicesRoadmapStops);
        final TableInfo _existingRoadmapStops = TableInfo.read(db, "roadmap_stops");
        if (!_infoRoadmapStops.equals(_existingRoadmapStops)) {
          return new RoomOpenHelper.ValidationResult(false, "roadmap_stops(com.eskisehir.events.data.local.entity.RoadmapStopEntity).\n"
                  + " Expected:\n" + _infoRoadmapStops + "\n"
                  + " Found:\n" + _existingRoadmapStops);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c4f81e67dec0512a557eae29ff4b932c", "6dbae4febcd3fb207ff641c4f919e2d9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorites","user_profile","comments","user_event_status","favorite_places","roadmap_stops");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `favorites`");
      _db.execSQL("DELETE FROM `user_profile`");
      _db.execSQL("DELETE FROM `comments`");
      _db.execSQL("DELETE FROM `user_event_status`");
      _db.execSQL("DELETE FROM `favorite_places`");
      _db.execSQL("DELETE FROM `roadmap_stops`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
    _typeConvertersMap.put(FavoriteDao.class, FavoriteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CommentDao.class, CommentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserEventStatusDao.class, UserEventStatusDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FavoritePlaceDao.class, FavoritePlaceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RoadmapStopDao.class, RoadmapStopDao_Impl.getRequiredConverters());
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
  public FavoriteDao favoriteDao() {
    if (_favoriteDao != null) {
      return _favoriteDao;
    } else {
      synchronized(this) {
        if(_favoriteDao == null) {
          _favoriteDao = new FavoriteDao_Impl(this);
        }
        return _favoriteDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public CommentDao commentDao() {
    if (_commentDao != null) {
      return _commentDao;
    } else {
      synchronized(this) {
        if(_commentDao == null) {
          _commentDao = new CommentDao_Impl(this);
        }
        return _commentDao;
      }
    }
  }

  @Override
  public UserEventStatusDao userEventStatusDao() {
    if (_userEventStatusDao != null) {
      return _userEventStatusDao;
    } else {
      synchronized(this) {
        if(_userEventStatusDao == null) {
          _userEventStatusDao = new UserEventStatusDao_Impl(this);
        }
        return _userEventStatusDao;
      }
    }
  }

  @Override
  public FavoritePlaceDao favoritePlaceDao() {
    if (_favoritePlaceDao != null) {
      return _favoritePlaceDao;
    } else {
      synchronized(this) {
        if(_favoritePlaceDao == null) {
          _favoritePlaceDao = new FavoritePlaceDao_Impl(this);
        }
        return _favoritePlaceDao;
      }
    }
  }

  @Override
  public RoadmapStopDao roadmapStopDao() {
    if (_roadmapStopDao != null) {
      return _roadmapStopDao;
    } else {
      synchronized(this) {
        if(_roadmapStopDao == null) {
          _roadmapStopDao = new RoadmapStopDao_Impl(this);
        }
        return _roadmapStopDao;
      }
    }
  }
}
