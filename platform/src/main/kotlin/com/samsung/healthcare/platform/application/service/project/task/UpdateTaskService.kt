package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.BadRequestException
import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskUseCase
import com.samsung.healthcare.platform.application.port.output.project.task.ItemOutputPort
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.project.task.Item
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.TaskStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UpdateTaskService(
    private val taskOutputPort: TaskOutputPort,
    private val itemOutputPort: ItemOutputPort
) : UpdateTaskUseCase {
    /**
     * Updates a [Task].
     *
     * If the task is published, additionally updates the [publishedAt][Task.publishedAt] field to reflect changes. Before that, checks that the user is authorized first.
     *
     * @param projectId id of the project.
     * @param taskId id of the Task to be updated.
     * @param revisionId [RevisionId] to be associated with this version of the Task.
     * @param command [UpdateTaskCommand]
     */
    @Transactional
    override suspend fun updateTask(
        projectId: String,
        taskId: String,
        revisionId: RevisionId,
        command: UpdateTaskCommand
    ) {
        Authorizer.getAccount(AccessProjectAuthority(projectId))
            .flatMap {
                mono {
                    val task: Task = if (command.status == TaskStatus.PUBLISHED)
                        Task(revisionId, taskId, command.properties, command.status, publishedAt = LocalDateTime.now())
                    else
                        Task(revisionId, taskId, command.properties, command.status)

                    if (taskOutputPort.findByIdAndRevisionId(taskId, revisionId) == null)
                        throw NotFoundException("Not Found")

                    taskOutputPort.findById(taskId).collect {
                        if (it.status === TaskStatus.PUBLISHED) {
                            throw BadRequestException("Published Task cannot be changed.")
                        }
                    }

                    taskOutputPort.update(
                        task
                    ).let { updatedTask ->
                        requireNotNull(updatedTask.revisionId)
                        itemOutputPort.update(
                            updatedTask.revisionId.value,
                            command.items.map {
                                requireNotNull(it.sequence)
                                Item.newItem(updatedTask, it.contents, it.type, it.sequence)
                            }
                        )
                    }
                }
            }.awaitSingle()
    }
}
