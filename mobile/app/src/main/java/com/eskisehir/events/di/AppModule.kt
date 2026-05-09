package com.eskisehir.events.di

import android.content.Context
import androidx.room.Room
import com.eskisehir.events.data.local.dao.CommentDao
import com.eskisehir.events.data.local.dao.FavoriteDao
import com.eskisehir.events.data.local.dao.FavoritePlaceDao
import com.eskisehir.events.data.local.dao.RoadmapStopDao
import com.eskisehir.events.data.local.dao.UserDao
import com.eskisehir.events.data.local.dao.UserEventStatusDao
import com.eskisehir.events.data.local.database.AppDatabase
import com.eskisehir.events.data.repository.EventRepositoryImpl
import com.eskisehir.events.data.repository.UserRepositoryImpl
import com.eskisehir.events.domain.repository.EventRepository
import com.eskisehir.events.domain.repository.UserRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing application-level dependencies:
 * Room database, DAOs, and repository binding.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "eskisehir_events_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideCommentDao(database: AppDatabase): CommentDao {
        return database.commentDao()
    }

    @Provides
    @Singleton
    fun provideUserEventStatusDao(database: AppDatabase): UserEventStatusDao {
        return database.userEventStatusDao()
    }

    @Provides
    @Singleton
    fun provideFavoritePlaceDao(database: AppDatabase): FavoritePlaceDao {
        return database.favoritePlaceDao()
    }

    @Provides
    @Singleton
    fun provideRoadmapStopDao(database: AppDatabase): RoadmapStopDao {
        return database.roadmapStopDao()
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository = impl

    @Provides
    @Singleton
    fun provideUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository = impl

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}
