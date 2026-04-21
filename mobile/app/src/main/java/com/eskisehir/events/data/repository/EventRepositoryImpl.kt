package com.eskisehir.events.data.repository

import com.eskisehir.events.data.local.dao.FavoriteDao
import com.eskisehir.events.data.local.entity.FavoriteEntity
import com.eskisehir.events.data.remote.api.EventApiService
import com.eskisehir.events.data.remote.dto.RecommendationRequestDto
import com.eskisehir.events.data.remote.dto.RouteRequestDto
import com.eskisehir.events.domain.model.Category
import com.eskisehir.events.domain.model.Event
import com.eskisehir.events.domain.repository.EventRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of EventRepository.
 * Combines remote API calls with local Room database for favorites.
 * 
 * Uses Kotlin Result type for error handling, allowing the presentation
 * layer to handle errors gracefully without try-catch blocks.
 */
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val apiService: EventApiService,
    private val favoriteDao: FavoriteDao
) : EventRepository {

    override suspend fun getEvents(): Result<List<Event>> {
        return try {
            val events = apiService.getEvents().map { it.toDomainModel() }
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventById(id: Long): Result<Event> {
        return try {
            val event = apiService.getEventById(id).toDomainModel()
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventsByCategory(category: Category): Result<List<Event>> {
        return try {
            val events = apiService.getEventsByCategory(category.name).map { it.toDomainModel() }
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchEvents(query: String): Result<List<Event>> {
        return try {
            val events = apiService.searchEvents(query).map { it.toDomainModel() }
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecommendations(
        categories: List<Category>,
        tags: List<String>,
        maxPrice: Double?,
        limit: Int
    ): Result<List<Event>> {
        return try {
            val request = RecommendationRequestDto(
                preferredCategories = categories.map { it.name },
                preferredTags = tags,
                maxPrice = maxPrice,
                limit = limit
            )
            val events = apiService.getRecommendations(request).map { it.toDomainModel() }
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRoute(
        eventIds: List<Long>,
        startLat: Double?,
        startLng: Double?
    ): Result<List<Event>> {
        return try {
            val request = RouteRequestDto(
                eventIds = eventIds,
                startLatitude = startLat,
                startLongitude = startLng
            )
            val events = apiService.getRoute(request).map { it.toDomainModel() }
            Result.success(events)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteIds(): Set<Long> {
        return favoriteDao.getAllFavoriteIds().toSet()
    }

    override suspend fun toggleFavorite(eventId: Long) {
        if (favoriteDao.isFavorite(eventId)) {
            favoriteDao.removeFavoriteById(eventId)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(eventId))
        }
    }

    override suspend fun isFavorite(eventId: Long): Boolean {
        return favoriteDao.isFavorite(eventId)
    }
}
