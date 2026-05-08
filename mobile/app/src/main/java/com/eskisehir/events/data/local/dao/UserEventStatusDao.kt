package com.eskisehir.events.data.local.dao

import androidx.room.*
import com.eskisehir.events.data.local.entity.EventStatus
import com.eskisehir.events.data.local.entity.UserEventStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserEventStatusDao {
    @Query("SELECT * FROM user_event_status WHERE eventId = :eventId")
    suspend fun getStatusForEvent(eventId: Long): UserEventStatusEntity?

    @Query("SELECT * FROM user_event_status WHERE eventId = :eventId")
    fun getStatusForEventFlow(eventId: Long): Flow<UserEventStatusEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setStatus(status: UserEventStatusEntity)

    @Query("SELECT eventId FROM user_event_status WHERE status = :status")
    suspend fun getEventIdsWithStatus(status: EventStatus): List<Long>

    @Query("SELECT * FROM user_event_status WHERE status = :status")
    fun getEventsWithStatusFlow(status: EventStatus): Flow<List<UserEventStatusEntity>>
}
