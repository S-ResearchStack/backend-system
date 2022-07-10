package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.platform.application.port.input.project.task.UploadTaskResultCommand
import com.samsung.healthcare.platform.application.port.input.project.task.UploadTaskResultUseCase
import com.samsung.healthcare.platform.application.port.output.project.task.ItemResultOutputPort
import com.samsung.healthcare.platform.application.port.output.project.task.TaskResultOutputPort
import com.samsung.healthcare.platform.domain.project.task.ItemResult
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.TaskResult
import org.springframework.stereotype.Service

@Service
class UploadTaskResultService(
    private val taskResultOutputPort: TaskResultOutputPort,
    private val itemResultOutputPort: ItemResultOutputPort,
) : UploadTaskResultUseCase {
    /**
     * Uploads [TaskResult]s.
     *
     * Each [UploadTaskResultCommand] includes the associated [UploadItemResultCommands][UploadTaskResultCommand.UpdateItemResultCommand].
     *
     * @param commands List of UploadTaskResultCommands.
     */
    override suspend fun uploadResults(commands: List<UploadTaskResultCommand>) {
        commands.forEach { taskResultCommand ->
            val taskResult = TaskResult.newTaskResult(
                RevisionId.from(taskResultCommand.revisionId),
                taskResultCommand.taskId,
                taskResultCommand.userId,
                taskResultCommand.startedAt,
                taskResultCommand.submittedAt,
            )
            taskResultOutputPort.create(taskResult)
            taskResultCommand.itemResults.forEach { itemResultCommand ->
                itemResultOutputPort.create(
                    ItemResult.newItemResult(
                        taskResult,
                        itemResultCommand.itemName,
                        itemResultCommand.result,
                    )
                )
            }
        }
    }
}
