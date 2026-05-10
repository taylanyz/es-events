package com.eskisehir.events.data.repository

import com.eskisehir.events.data.local.dao.SavedRouteDao
import com.eskisehir.events.data.local.entity.SavedRouteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedRouteRepository @Inject constructor(
    private val savedRouteDao: SavedRouteDao
) {
    fun getSavedRoutesByUser(email: String): Flow<List<SavedRouteEntity>> {
        return savedRouteDao.getSavedRoutesByUser(email)
    }

    suspend fun getSavedRouteById(id: Long): SavedRouteEntity? {
        return savedRouteDao.getSavedRouteById(id)
    }

    suspend fun saveRoute(route: SavedRouteEntity) {
        savedRouteDao.insertSavedRoute(route)
    }

    suspend fun deleteRoute(id: Long) {
        savedRouteDao.deleteSavedRouteById(id)
    }
}
