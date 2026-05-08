package com.eskisehir.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EventStatus {
    ATTENDED, GOING, WANT_TO_GO, NONE
}

@Entity(tableName = "user_event_status")
data class UserEventStatusEntity(
    @PrimaryKey
    val eventId: Long,
    val status: EventStatus
)
