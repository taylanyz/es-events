package com.eskisehir.events.data.remote.dto

import com.eskisehir.events.domain.model.Category
import com.eskisehir.events.domain.model.Event
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Data Transfer Object matching the backend's EventResponse JSON structure.
 * Handles JSON deserialization and mapping to the domain Event model.
 */
data class EventDto(
    val id: Long,
    val name: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val venue: String,
    val date: String,
    val price: Double,
    val imageUrl: String?,
    val tags: List<String>?
) {
    /**
     * Maps this DTO to the domain Event model.
     */
    fun toDomainModel(): Event {
        return Event(
            id = id,
            name = name,
            description = description,
            category = try {
                Category.valueOf(category)
            } catch (e: Exception) {
                Category.OTHER
            },
            latitude = latitude,
            longitude = longitude,
            venue = venue,
            date = try {
                LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (e: Exception) {
                LocalDateTime.now()
            },
            price = price,
            imageUrl = imageUrl ?: "",
            tags = tags ?: emptyList()
        )
    }
}

/**
 * Request body for the recommendation endpoint.
 */
data class RecommendationRequestDto(
    val preferredCategories: List<String>,
    val preferredTags: List<String>,
    val maxPrice: Double?,
    val limit: Int
)

/**
 * Request body for the route planning endpoint.
 */
data class RouteRequestDto(
    val eventIds: List<Long>,
    val startLatitude: Double?,
    val startLongitude: Double?
)
