package com.eskisehir.events.di;

/**
 * Hilt module providing application-level dependencies:
 * Room database, DAOs, and repository binding.
 */
@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/eskisehir/events/di/AppModule;", "", "()V", "provideAppDatabase", "Lcom/eskisehir/events/data/local/database/AppDatabase;", "context", "Landroid/content/Context;", "provideEventRepository", "Lcom/eskisehir/events/domain/repository/EventRepository;", "impl", "Lcom/eskisehir/events/data/repository/EventRepositoryImpl;", "provideFavoriteDao", "Lcom/eskisehir/events/data/local/dao/FavoriteDao;", "database", "app_debug"})
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
    public final com.eskisehir.events.domain.repository.EventRepository provideEventRepository(@org.jetbrains.annotations.NotNull()
    com.eskisehir.events.data.repository.EventRepositoryImpl impl) {
        return null;
    }
}