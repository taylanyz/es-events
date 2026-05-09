package com.eskisehir.events.data.local.dao

import androidx.room.*
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoadmapStopDao {
    @Query("SELECT * FROM roadmap_stops ORDER BY stopOrder ASC")
    fun getAllStops(): Flow<List<RoadmapStopEntity>>

    @Query("SELECT * FROM roadmap_stops WHERE eventId = :eventId LIMIT 1")
    suspend fun getStopByEventId(eventId: Long): RoadmapStopEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStop(stop: RoadmapStopEntity)

    @Delete
    suspend fun deleteStop(stop: RoadmapStopEntity)

    @Query("DELETE FROM roadmap_stops")
    suspend fun deleteAllStops()

    @Query("SELECT COUNT(*) FROM roadmap_stops")
    suspend fun getCount(): Int

    @Query("SELECT * FROM roadmap_stops ORDER BY stopOrder ASC")
    suspend fun getAllStopsOnce(): List<RoadmapStopEntity>

    @Update
    suspend fun updateStop(stop: RoadmapStopEntity)
}
