package com.samsung.healthcare.platform.adapter.persistence.entity.project.task

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.TaskResultMapper
import com.samsung.healthcare.platform.domain.project.task.TaskResult
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("task_results")
data class TaskResultEntity(
    @Id
    val id: Int?,
    val revisionId: Int,
    val taskId: String,
    val userId: String,
    val startedAt: LocalDateTime,
    val submittedAt: LocalDateTime,
)

fun TaskResult.toEntity(): TaskResultEntity = TaskResultMapper.INSTANCE.toEntity(this)
