package com.eskisehir.events.data.local.dao

import androidx.room.*
import com.eskisehir.events.data.local.entity.FavoritePlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePlaceDao {
    @Query("SELECT * FROM favorite_places")
    fun getAllFavoritePlaces(): Flow<List<FavoritePlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoritePlace(place: FavoritePlaceEntity)

    @Delete
    suspend fun removeFavoritePlace(place: FavoritePlaceEntity)
}
