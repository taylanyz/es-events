package com.eskisehir.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val email: String,
    val fullName: String?,
    val profileImageUri: String?,
    val interests: List<String> = emptyList()
)
