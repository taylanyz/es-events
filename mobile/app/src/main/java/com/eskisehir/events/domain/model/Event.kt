package com.eskisehir.events.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing an event in Eskişehir.
 * This is a pure Kotlin data class with no framework dependencies,
 * following Clean Architecture principles.
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
    val date: LocalDateTime,
    val price: Double,
    val imageUrl: String,
    val tags: List<String>,
    val isFavorite: Boolean = false,
    val isFeatured: Boolean = false
)
