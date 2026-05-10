package com.eskisehir.events.data.local.database;

/**
 * Room database for local storage.
 * Stores favorites, profile, comments, and event statuses.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&\u00a8\u0006\u000f"}, d2 = {"Lcom/eskisehir/events/data/local/database/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "commentDao", "Lcom/eskisehir/events/data/local/dao/CommentDao;", "favoriteDao", "Lcom/eskisehir/events/data/local/dao/FavoriteDao;", "favoritePlaceDao", "Lcom/eskisehir/events/data/local/dao/FavoritePlaceDao;", "roadmapStopDao", "Lcom/eskisehir/events/data/local/dao/RoadmapStopDao;", "userDao", "Lcom/eskisehir/events/data/local/dao/UserDao;", "userEventStatusDao", "Lcom/eskisehir/events/data/local/dao/UserEventStatusDao;", "app_debug"})
@androidx.room.Database(entities = {com.eskisehir.events.data.local.entity.FavoriteEntity.class, com.eskisehir.events.data.local.entity.UserProfileEntity.class, com.eskisehir.events.data.local.entity.CommentEntity.class, com.eskisehir.events.data.local.entity.UserEventStatusEntity.class, com.eskisehir.events.data.local.entity.FavoritePlaceEntity.class, com.eskisehir.events.data.local.entity.RoadmapStopEntity.class}, version = 4, exportSchema = false)
@androidx.room.TypeConverters(value = {com.eskisehir.events.data.local.database.Converters.class})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.eskisehir.events.data.local.dao.FavoriteDao favoriteDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.eskisehir.events.data.local.dao.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.eskisehir.events.data.local.dao.CommentDao commentDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.eskisehir.events.data.local.dao.UserEventStatusDao userEventStatusDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.eskisehir.events.data.local.dao.FavoritePlaceDao favoritePlaceDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.eskisehir.events.data.local.dao.RoadmapStopDao roadmapStopDao();
}