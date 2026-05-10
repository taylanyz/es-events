package com.eskisehir.eventapp.data.model

data class RoutePlaceSuggestion(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double?,
    val userRatingCount: Int?,
    val primaryType: String?,
    val types: List<String>,
    val reason: String,
    val segmentIndex: Int,
    val fromEventId: Long,
    val toEventId: Long
)
