package com.eskisehir.events.domain.usecase

import com.eskisehir.events.domain.model.Category
import com.eskisehir.events.domain.model.Event
import com.eskisehir.events.domain.repository.EventRepository
import javax.inject.Inject

/**
 * Use case for fetching all events, optionally filtered by category.
 * Enriches events with favorite status from local storage.
 */
class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(category: Category? = null): Result<List<Event>> {
        val result = if (category != null) {
            repository.getEventsByCategory(category)
        } else {
            repository.getEvents()
        }

        return result.map { events ->
            val favoriteIds = repository.getFavoriteIds()
            events.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
        }
    }
}

/**
 * Use case for fetching a single event by its ID.
 */
class GetEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: Long): Result<Event> {
        return repository.getEventById(id).map { event ->
            event.copy(isFavorite = repository.isFavorite(event.id))
        }
    }
}

/**
 * Use case for getting personalized event recommendations.
 * Delegates to the backend recommendation engine.
 */
class GetRecommendedEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        categories: List<Category>,
        tags: List<String>,
        maxPrice: Double? = null,
        limit: Int = 10
    ): Result<List<Event>> {
        return repository.getRecommendations(categories, tags, maxPrice, limit).map { events ->
            val favoriteIds = repository.getFavoriteIds()
            events.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
        }
    }
}

/**
 * Use case for toggling an event's favorite status.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: Long) {
        repository.toggleFavorite(eventId)
    }
}

/**
 * Use case for searching events by a query string.
 */
class SearchEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(query: String): Result<List<Event>> {
        return repository.searchEvents(query).map { events ->
            val favoriteIds = repository.getFavoriteIds()
            events.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
        }
    }
}
