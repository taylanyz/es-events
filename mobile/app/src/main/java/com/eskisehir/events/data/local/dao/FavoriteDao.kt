package com.eskisehir.events.data.local.dao

import androidx.room.*
import com.eskisehir.events.data.local.entity.FavoriteEntity

/**
 * Data Access Object for the favorites table.
 * Provides methods to add, remove, query, and check favorite events.
 */
@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavoriteEntity>

    @Query("SELECT eventId FROM favorites")
    suspend fun getAllFavoriteIds(): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE eventId = :eventId)")
    suspend fun isFavorite(eventId: Long): Boolean

    @Query("DELETE FROM favorites WHERE eventId = :eventId")
    suspend fun removeFavoriteById(eventId: Long)
}
