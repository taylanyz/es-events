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
    val tags: List<String>?,
    val environmentType: String = "karma",
    val difficultyLevel: String = "başlangıç",
    val groupSizeType: String = "her biri",
    val activityLevel: String = "hafif",
    val socialAspect: String = "her biri",
    val isWheelchairAccessible: Boolean = false,
    val hasParking: Boolean = false,
    val hasPublicTransport: Boolean = false,
    val allowsPhotography: Boolean = true,
    val hasFoodDrink: Boolean = false,
    val language: String? = null,
    val duration: Int = 120
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

/**
 * Request body for logging user interactions (clicks).
 */
data class InteractionRequestDto(
    val eventId: Long,
    val clicked: Boolean = true
)

/**
 * Request body for smart recommendations with UI-based criteria selection.
 */
data class SmartRecommendationRequestDto(
    val userId: Long,
    val limit: Int = 5,
    // Budget
    val maxPrice: Double? = null,
    val minPrice: Double? = null,
    // Duration
    val minDuration: Int? = null,
    val maxDuration: Int? = null,
    // Environment & Atmosphere
    val environmentType: String? = null,
    val crowdSize: String? = null,
    val isIndoor: Boolean? = null,
    // Activity & Difficulty
    val activityLevel: String? = null,
    val difficultyLevel: String? = null,
    // Social & Group
    val socialAspect: String? = null,
    val groupSize: String? = null,
    // Culture & Age
    val ageGroup: String? = null,
    val culturalValue: String? = null,
    // Time & Weather
    val bestTimeOfDay: String? = null,
    val weatherDependent: Boolean? = null,
    // Accessibility
    val requireWheelchair: Boolean? = null,
    val requireParking: Boolean? = null,
    val requireTransport: Boolean? = null,
    val requirePhotography: Boolean? = null,
    val requireFood: Boolean? = null
)

/**
 * Response body for smart recommendations.
 */
data class RecommendationResponseDto(
    val eventId: Long,
    val eventName: String,
    val finalScore: Double,
    val semanticScore: Double,
    val thompsonScore: Double,
    val explanation: String,
    val tags: List<String>
)
