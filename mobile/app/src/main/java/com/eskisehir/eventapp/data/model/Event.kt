package com.eskisehir.eventapp.data.model

/**
 * Event data class matching the backend EventResponse DTO.
 * Augmented with metadata for AI Recommendation System.
 */
data class Event(
    val id: Long,
    val name: String,
    val description: String,
    val category: Category,
    val latitude: Double,
    val longitude: Double,
    val venue: String,
    val address: String = "",
    val date: String, // ISO 8601 (e.g. 2026-05-15T20:00)
    val price: Double,
    val imageUrl: String?,
    val tags: List<String>?,
    val isFeatured: Boolean = false,
    
    // Metadata for AI Recommendations
    val priceLevel: Int = 0, // 0: Free, 1: Low, 2: Medium, 3: High
    val crowdLevel: Int = 1, // 1: Quiet, 2: Medium, 3: Social/Crowded
    val isIndoor: Boolean = true,
    val hasParking: Boolean = false,
    val publicTransportFriendly: Boolean = true,
    val durationMinutes: Int = 120, // Short < 60, Medium 60-180, Long > 180
    
    // Extended Metadata for Discovery
    val preferredTimeOfDay: String = "evening", // morning, afternoon, evening, night
    val socialMood: String = "fun", // quiet, social, romantic, discovery, fun
    val suitableFor: List<String> = listOf("friends"), // alone, friends, family, couple, group
    
    // Legacy / Detailed fields
    val environmentType: String = "karma",
    val difficultyLevel: String = "başlangıç",
    val groupSizeType: String = "her biri",
    val activityLevel: String = "hafif",
    val socialAspect: String = "her biri",
    val isWheelchairAccessible: Boolean = false,
    val allowsPhotography: Boolean = true,
    val hasFoodDrink: Boolean = false,
    val language: String? = null
)
