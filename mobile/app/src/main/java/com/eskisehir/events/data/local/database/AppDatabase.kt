package com.eskisehir.events.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eskisehir.events.data.local.dao.CommentDao
import com.eskisehir.events.data.local.dao.FavoriteDao
import com.eskisehir.events.data.local.dao.FavoritePlaceDao
import com.eskisehir.events.data.local.dao.RoadmapStopDao
import com.eskisehir.events.data.local.dao.UserDao
import com.eskisehir.events.data.local.dao.UserEventStatusDao
import com.eskisehir.events.data.local.entity.CommentEntity
import com.eskisehir.events.data.local.entity.FavoriteEntity
import com.eskisehir.events.data.local.entity.FavoritePlaceEntity
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import com.eskisehir.events.data.local.entity.UserEventStatusEntity
import com.eskisehir.events.data.local.entity.UserProfileEntity

/**
 * Room database for local storage.
 * Stores favorites, profile, comments, and event statuses.
 */
@Database(
    entities = [
        FavoriteEntity::class,
        UserProfileEntity::class,
        CommentEntity::class,
        UserEventStatusEntity::class,
        FavoritePlaceEntity::class,
        RoadmapStopEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun userEventStatusDao(): UserEventStatusDao
    abstract fun favoritePlaceDao(): FavoritePlaceDao
    abstract fun roadmapStopDao(): RoadmapStopDao
}
