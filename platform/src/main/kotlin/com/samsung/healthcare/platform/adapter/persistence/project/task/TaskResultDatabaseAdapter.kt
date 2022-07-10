package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.application.port.output.project.task.TaskResultOutputPort
import com.samsung.healthcare.platform.domain.project.task.TaskResult
import org.springframework.stereotype.Component

@Component
class TaskResultDatabaseAdapter(
    private val taskResultRepository: TaskResultRepository,
) : TaskResultOutputPort {
    override suspend fun create(taskResult: TaskResult) {
        taskResultRepository.save(taskResult.toEntity())
    }
}
