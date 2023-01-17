package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskResponse
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskUseCase
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.project.task.Task
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service

@Service
class CreateTaskService(
    private val taskOutputPort: TaskOutputPort
) : CreateTaskUseCase {

    /**
     * Creates a new [Task].
     *
     * A new task is initialized as an empty Task in draft status. Before that, checks that the user is authorized first.
     *
     * @param projectId The id of the project
     * @return [CreateTaskResponse] data class
     */
    override suspend fun createTask(projectId: String): CreateTaskResponse =
        Authorizer.getAccount(AccessProjectAuthority(projectId))
            .flatMap {
                mono {
                    taskOutputPort.create(
                        Task.newTask()
                    ).let {
                        CreateTaskResponse(
                            it.revisionId?.value,
                            it.id
                        )
                    }
                }
            }.awaitSingle()
}
