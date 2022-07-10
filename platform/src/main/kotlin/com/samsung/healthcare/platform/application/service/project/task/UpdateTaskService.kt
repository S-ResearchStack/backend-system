package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskUseCase
import com.samsung.healthcare.platform.application.port.output.project.task.ItemOutputPort
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.project.task.Item
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.TaskStatus
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
     * If the task is published, additionally updates the [publishedAt][Task.publishedAt] field to reflect changes.
     *
     * @param id id of the Task to be updated.
     * @param revisionId [RevisionId] to be associated with this version of the Task.
     * @param command [UpdateTaskCommand]
     */
    @Transactional
    override suspend fun updateTask(
        id: String,
        revisionId: RevisionId,
        command: UpdateTaskCommand
    ) {
        val task: Task = if (command.status == TaskStatus.PUBLISHED)
            Task(revisionId, id, command.properties, command.status, publishedAt = LocalDateTime.now())
        else
            Task(revisionId, id, command.properties, command.status)

        taskOutputPort.update(
            task
        ).let { task ->
            requireNotNull(task.revisionId)
            itemOutputPort.update(
                task.revisionId.value,
                command.items.map {
                    Item.newItem(task, it.contents, it.type, it.sequence)
                }
            )
        }
    }
}
