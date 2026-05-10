package com.eskisehir.events.di;

/**
 * Hilt module providing application-level dependencies:
 * Room database, DAOs, and repository binding.
 */
@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0012\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\f\u001a\u00020\u001eH\u0007\u00a8\u0006\u001f"}, d2 = {"Lcom/eskisehir/events/di/AppModule;", "", "()V", "provideAppDatabase", "Lcom/eskisehir/events/data/local/database/AppDatabase;", "context", "Landroid/content/Context;", "provideCommentDao", "Lcom/eskisehir/events/data/local/dao/CommentDao;", "database", "provideEventRepository", "Lcom/eskisehir/events/domain/repository/EventRepository;", "impl", "Lcom/eskisehir/events/data/repository/EventRepositoryImpl;", "provideFavoriteDao", "Lcom/eskisehir/events/data/local/dao/FavoriteDao;", "provideFavoritePlaceDao", "Lcom/eskisehir/events/data/local/dao/FavoritePlaceDao;", "provideFusedLocationProviderClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "provideRoadmapStopDao", "Lcom/eskisehir/events/data/local/dao/RoadmapStopDao;", "provideSavedRouteDao", "Lcom/eskisehir/events/data/local/dao/SavedRouteDao;", "provideUserDao", "Lcom/eskisehir/events/data/local/dao/UserDao;", "provideUserEventStatusDao", "Lcom/eskisehir/events/data/local/dao/UserEventStatusDao;", "provideUserRepository", "Lcom/eskisehir/events/domain/repository/UserRepository;", "Lcom/eskisehir/events/data/repository/UserRepositoryImpl;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.eskisehir.events.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.database.AppDatabase provideAppDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.dao.FavoriteDao provideFavoriteDao(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.dao.UserDao provideUserDao(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.dao.CommentDao provideCommentDao(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.dao.UserEventStatusDao provideUserEventStatusDao(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.dao.FavoritePlaceDao provideFavoritePlaceDao(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.dao.RoadmapStopDao provideRoadmapStopDao(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.data.local.dao.SavedRouteDao provideSavedRouteDao(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.local.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.domain.repository.EventRepository provideEventRepository(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.repository.EventRepositoryImpl impl) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.eskisehir.events.domain.repository.UserRepository provideUserRepository(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.repository.UserRepositoryImpl impl) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.google.android.gms.location.FusedLocationProviderClient provideFusedLocationProviderClient(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}