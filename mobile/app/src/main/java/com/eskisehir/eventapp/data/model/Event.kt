package com.eskisehir.eventapp.data.model

/**
 * Event categories matching the backend Category enum.
 */
enum class Category(val displayNameTr: String) {
    CONCERT("Konser"),
    THEATER("Tiyatro"),
    EXHIBITION("Sergi"),
    FESTIVAL("Festival"),
    WORKSHOP("Atölye"),
    SPORTS("Spor"),
    STANDUP("Stand-up"),
    CINEMA("Sinema"),
    CONFERENCE("Konferans"),
    OTHER("Diğer")
}

/**
 * Event data class matching the backend EventResponse DTO.
 */
data class Event(
    val id: Long,
    val name: String,
    val description: String,
    val category: Category,
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
)
