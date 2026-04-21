package com.eskisehir.events.di

import android.content.Context
import androidx.room.Room
import com.eskisehir.events.data.local.dao.FavoriteDao
import com.eskisehir.events.data.local.database.AppDatabase
import com.eskisehir.events.data.repository.EventRepositoryImpl
import com.eskisehir.events.domain.repository.EventRepository
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
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository = impl
}
