package com.eskisehir.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eskisehir.eventapp.data.model.SavedRouteSegment
import com.eskisehir.eventapp.data.model.SavedRouteStop

@Entity(tableName = "saved_routes")
data class SavedRouteEntity(
    @PrimaryKey(autoGenerate = true)
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
