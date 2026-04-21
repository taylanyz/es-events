package com.eskisehir.events.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eskisehir.events.data.local.dao.FavoriteDao
import com.eskisehir.events.data.local.entity.FavoriteEntity

/**
 * Room database for local storage.
 * Currently only stores favorite event IDs.
 */
@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
