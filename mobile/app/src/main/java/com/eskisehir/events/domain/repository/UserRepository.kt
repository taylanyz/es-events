package com.eskisehir.events.domain.repository

import com.eskisehir.events.data.local.entity.CommentEntity
import com.eskisehir.events.data.local.entity.EventStatus
import com.eskisehir.events.data.local.entity.FavoritePlaceEntity
import com.eskisehir.events.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(): Flow<UserProfileEntity?>
    suspend fun updateProfile(profile: UserProfileEntity)
    
    fun getComments(eventId: Long): Flow<List<CommentEntity>>
    suspend fun addComment(comment: CommentEntity)
    
    fun getStatusForEvent(eventId: Long): Flow<EventStatus>
    suspend fun setEventStatus(eventId: Long, status: EventStatus)
    suspend fun getEventIdsWithStatus(status: EventStatus): List<Long>
    
    fun getFavoritePlaces(): Flow<List<FavoritePlaceEntity>>
    suspend fun addFavoritePlace(name: String, location: String, category: String?)
    suspend fun removeFavoritePlace(place: FavoritePlaceEntity)
}
