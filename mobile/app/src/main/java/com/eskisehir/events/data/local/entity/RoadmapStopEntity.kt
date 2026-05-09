package com.eskisehir.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roadmap_stops")
data class RoadmapStopEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val address: String,
    val stopOrder: Int // Order in roadmap
)
