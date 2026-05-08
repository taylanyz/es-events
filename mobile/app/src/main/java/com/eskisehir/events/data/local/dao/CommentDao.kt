package com.eskisehir.events.data.local.dao

import androidx.room.*
import com.eskisehir.events.data.local.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE eventId = :eventId ORDER BY timestamp DESC")
    fun getCommentsForEvent(eventId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)
}
