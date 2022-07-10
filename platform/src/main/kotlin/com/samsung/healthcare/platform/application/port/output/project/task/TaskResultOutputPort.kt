package com.samsung.healthcare.platform.application.port.output.project.task

import com.samsung.healthcare.platform.domain.project.task.TaskResult

interface TaskResultOutputPort {
    suspend fun create(taskResult: TaskResult)
}
