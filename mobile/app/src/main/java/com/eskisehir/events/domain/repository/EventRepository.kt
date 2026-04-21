package com.eskisehir.events.domain.repository

import com.eskisehir.events.domain.model.Category
import com.eskisehir.events.domain.model.Event

/**
 * Repository contract for the domain layer.
 * Defines what data operations are available without specifying
 * how they are implemented (API, database, cache, etc.).
 */
interface EventRepository {

    /** Fetches all events from the backend */
    suspend fun getEvents(): Result<List<Event>>

    /** Fetches a single event by ID */
    suspend fun getEventById(id: Long): Result<Event>

    /** Fetches events filtered by category */
    suspend fun getEventsByCategory(category: Category): Result<List<Event>>

    /** Searches events by query string */
    suspend fun searchEvents(query: String): Result<List<Event>>

    /** Gets personalized recommendations based on user preferences */
    suspend fun getRecommendations(
        categories: List<Category>,
        tags: List<String>,
        maxPrice: Double? = null,
        limit: Int = 10
    ): Result<List<Event>>

    /** Gets events in optimized route order */
    suspend fun getRoute(
        eventIds: List<Long>,
        startLat: Double? = null,
        startLng: Double? = null
    ): Result<List<Event>>

    /** Gets all favorite event IDs from local storage */
    suspend fun getFavoriteIds(): Set<Long>

    /** Toggles favorite status for an event */
    suspend fun toggleFavorite(eventId: Long)

    /** Checks if an event is favorited */
    suspend fun isFavorite(eventId: Long): Boolean
}
