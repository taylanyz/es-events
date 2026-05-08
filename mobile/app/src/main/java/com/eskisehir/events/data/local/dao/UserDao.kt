package com.eskisehir.events.data.local.dao

import androidx.room.*
import com.eskisehir.events.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET profileImageUri = :uri WHERE email = :email")
    suspend fun updateProfileImage(email: String, uri: String)
}
