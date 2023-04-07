package com.samsung.healthcare.platform.adapter.persistence.entity.project.task

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.TaskMapper
import com.samsung.healthcare.platform.domain.project.task.Task
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tasks")
data class TaskEntity(
    @Id
    var revisionId: Int?,
    val id: String,
    var properties: Map<String, Any>,
    var status: String,
    val type: String,
    @CreatedDate
    var createdAt: LocalDateTime?,
    var publishedAt: LocalDateTime?,
    var outdatedAt: LocalDateTime?,
    var deletedAt: LocalDateTime?
) {
    fun toDomain(): Task = TaskMapper.INSTANCE.toDomain(this)
}

fun Task.toEntity(): TaskEntity = TaskMapper.INSTANCE.toEntity(this)
