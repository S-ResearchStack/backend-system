package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.TaskEntity
import com.samsung.healthcare.platform.enums.TaskStatus
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime

interface TaskRepository : CoroutineCrudRepository<TaskEntity, Int> {
    suspend fun findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): Flow<TaskEntity>

    suspend fun findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndStatus(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        status: String,
    ): Flow<TaskEntity>

    suspend fun findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndType(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        type: String,
    ): Flow<TaskEntity>

    suspend fun findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndStatusAndType(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        status: String,
        type: String,
    ): Flow<TaskEntity>

    suspend fun findByPublishedAtGreaterThanEqualAndPublishedAtLessThanAndStatusEquals(
        lastSyncTime: LocalDateTime,
        currentTime: LocalDateTime,
        status: String = TaskStatus.PUBLISHED.name
    ): Flow<TaskEntity>

    suspend fun findByIdIn(id: List<String>): Flow<TaskEntity>

    suspend fun findByIdAndRevisionId(id: String, revisionId: Int): TaskEntity?
}
