package com.eskisehir.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long,
    val userEmail: String,
    val userName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
