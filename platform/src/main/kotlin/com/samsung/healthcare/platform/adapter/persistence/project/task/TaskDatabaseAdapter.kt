package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TaskDatabaseAdapter(
    private val taskRepository: TaskRepository
) : TaskOutputPort {
    override suspend fun findByPeriod(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        status: String?,
        type: String?,
    ): Flow<Task> {
        return if (status != null && type != null) {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndStatusAndType(
                startTime,
                endTime,
                status,
                type
            )
                .map { it.toDomain() }
        } else if (status != null) {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndStatus(startTime, endTime, status)
                .map { it.toDomain() }
        } else if (type != null) {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndType(startTime, endTime, type)
                .map { it.toDomain() }
        } else {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(startTime, endTime)
                .map { it.toDomain() }
        }
    }

    override suspend fun findByPublishedAt(lastSyncTime: LocalDateTime, currentTime: LocalDateTime): Flow<Task> =
        taskRepository.findByPublishedAtGreaterThanEqualAndPublishedAtLessThanAndStatusEquals(lastSyncTime, currentTime)
            .map { it.toDomain() }

    override suspend fun findById(id: String): Flow<Task> {
        return taskRepository.findByIdIn(listOf(id)).map { it.toDomain() }
    }

    override suspend fun findByIdAndRevisionId(id: String, revisionId: RevisionId): Task? =
        taskRepository.findByIdAndRevisionId(id, revisionId.value)?.toDomain()

    override suspend fun create(task: Task): Task =
        taskRepository.save(task.toEntity()).toDomain()

    override suspend fun update(task: Task): Task {
        requireNotNull(task.revisionId)
        return taskRepository.findById(task.revisionId.value)?.copy(
            properties = task.properties,
            status = task.status.name,
            type = task.type.name,
            publishedAt = task.publishedAt
        ).let {
            requireNotNull(it)
            taskRepository.save(it).toDomain()
        }
    }
}
