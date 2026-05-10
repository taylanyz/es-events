package com.eskisehir.eventapp.data.model

data class SavedRoute(
    val id: Long = 0,
    val userEmail: String,
    val title: String,
    val date: String,
    val createdAt: Long = System.currentTimeMillis(),
    val totalDurationSeconds: Long,
    val totalDistanceMeters: Long,
    val travelMode: String,
    val stops: List<SavedRouteStop>,
    val segments: List<SavedRouteSegment>
)

data class SavedRouteStop(
    val eventId: Long,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val address: String,
    val stopOrder: Int,
    val isAiRecommended: Boolean = false
)

data class SavedRouteSegment(
    val fromIndex: Int,
    val toIndex: Int,
    val durationSeconds: Long,
    val distanceMeters: Long,
    val encodedPolyline: String
)
