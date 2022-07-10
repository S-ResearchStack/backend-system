package com.samsung.healthcare.platform.application.port.output.project.task

import com.samsung.healthcare.platform.domain.project.task.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TaskOutputPort {
    suspend fun findByPeriod(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        status: String?,
    ): Flow<Task>

    suspend fun findByPublishedAt(
        lastSyncTime: LocalDateTime,
        currentTime: LocalDateTime
    ): Flow<Task>

    suspend fun findById(id: String): Flow<Task>

    suspend fun create(task: Task): Task

    suspend fun update(task: Task): Task
}
