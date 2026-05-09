package com.eskisehir.events.data.repository

import com.eskisehir.events.data.local.dao.RoadmapStopDao
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoadmapRepository @Inject constructor(
    private val roadmapStopDao: RoadmapStopDao
) {
    fun getAllStops(): Flow<List<RoadmapStopEntity>> = roadmapStopDao.getAllStops()

    suspend fun addStop(
        eventId: Long,
        title: String,
        latitude: Double,
        longitude: Double,
        locationName: String,
        address: String
    ) {
        val maxOrder = roadmapStopDao.getAllStopsOnce().maxOfOrNull { it.stopOrder } ?: -1
        val stop = RoadmapStopEntity(
            eventId = eventId,
            title = title,
            latitude = latitude,
            longitude = longitude,
            locationName = locationName,
            address = address,
            stopOrder = maxOrder + 1
        )
        roadmapStopDao.insertStop(stop)
    }

    suspend fun removeStop(eventId: Long) {
        val stop = roadmapStopDao.getStopByEventId(eventId) ?: return
        roadmapStopDao.deleteStop(stop)

        // Reorder remaining stops
        val allStops = roadmapStopDao.getAllStopsOnce()
            .sortedBy { it.stopOrder }
            .mapIndexed { index, entity ->
                entity.copy(stopOrder = index)
            }
        allStops.forEach { roadmapStopDao.updateStop(it) }
    }

    suspend fun isEventInRoadmap(eventId: Long): Boolean {
        return roadmapStopDao.getStopByEventId(eventId) != null
    }

    suspend fun getAllStopsOnce(): List<RoadmapStopEntity> {
        return roadmapStopDao.getAllStopsOnce()
    }

    suspend fun clearRoadmap() {
        roadmapStopDao.deleteAllStops()
    }

    suspend fun reorderStops(stops: List<RoadmapStopEntity>) {
        stops.forEachIndexed { index, stop ->
            roadmapStopDao.updateStop(stop.copy(stopOrder = index))
        }
    }
}
