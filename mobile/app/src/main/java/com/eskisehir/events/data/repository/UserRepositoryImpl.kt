package com.eskisehir.events.data.repository

import com.eskisehir.events.data.local.dao.CommentDao
import com.eskisehir.events.data.local.dao.FavoritePlaceDao
import com.eskisehir.events.data.local.dao.UserDao
import com.eskisehir.events.data.local.dao.UserEventStatusDao
import com.eskisehir.events.data.local.entity.*
import com.eskisehir.events.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val commentDao: CommentDao,
    private val statusDao: UserEventStatusDao,
    private val placeDao: FavoritePlaceDao
) : UserRepository {

    override fun getUserProfile(): Flow<UserProfileEntity?> = userDao.getUserProfile()

    override suspend fun updateProfile(profile: UserProfileEntity) {
        userDao.insertOrUpdateProfile(profile)
    }

    override fun getComments(eventId: Long): Flow<List<CommentEntity>> = 
        commentDao.getCommentsForEvent(eventId)

    override suspend fun addComment(comment: CommentEntity) {
        commentDao.insertComment(comment)
    }

    override fun getStatusForEvent(eventId: Long): Flow<EventStatus> =
        statusDao.getStatusForEventFlow(eventId).map { it?.status ?: EventStatus.NONE }

    override suspend fun setEventStatus(eventId: Long, status: EventStatus) {
        statusDao.setStatus(UserEventStatusEntity(eventId, status))
    }

    override suspend fun getEventIdsWithStatus(status: EventStatus): List<Long> =
        statusDao.getEventIdsWithStatus(status)

    override fun getFavoritePlaces(): Flow<List<FavoritePlaceEntity>> =
        placeDao.getAllFavoritePlaces()

    override suspend fun addFavoritePlace(name: String, location: String, category: String?) {
        placeDao.addFavoritePlace(FavoritePlaceEntity(name = name, location = location, category = category))
    }

    override suspend fun removeFavoritePlace(place: FavoritePlaceEntity) {
        placeDao.removeFavoritePlace(place)
    }

    override suspend fun clearUserProfile() {
        userDao.clearAllProfiles()
    }
}
