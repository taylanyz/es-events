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
    val date: String,       // ISO date string for simplicity
    val price: Double,
    val imageUrl: String,
    val tags: List<String>
)
