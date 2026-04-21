package com.eskisehir.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for storing favorited event IDs locally.
 * We only store the ID — event data is always fetched from the backend.
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val eventId: Long
)
