package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskResponse
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskUseCase
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.project.task.Task
import org.springframework.stereotype.Service

@Service
class CreateTaskService(
    private val taskOutputPort: TaskOutputPort
) : CreateTaskUseCase {

    /**
     * Creates a new [Task].
     *
     * The created task is initialized as an empty Task in draft status.
     *
     * @return [CreateTaskResponse] data class
     */
    override suspend fun createTask(): CreateTaskResponse =
        taskOutputPort.create(
            Task.newTask()
        ).let {
            CreateTaskResponse(
                it.revisionId?.value,
                it.id
            )
        }
}
