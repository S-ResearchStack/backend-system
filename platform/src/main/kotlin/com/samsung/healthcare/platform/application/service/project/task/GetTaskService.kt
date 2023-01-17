package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.BadRequestException
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskUseCase
import com.samsung.healthcare.platform.application.port.output.project.task.ItemOutputPort
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class GetTaskService(
    private val taskOutputPort: TaskOutputPort,
    private val itemOutputPort: ItemOutputPort
) : GetTaskUseCase {
    /**
     * Returns relevant task results that have created time between the startTime and endTime specified by [GetTaskCommand]. First checks that the user is authorized, then utilizes [byCreatedAt].
     *
     * @param projectId The id of the project
     * @param command [GetTaskCommand] with request specifications.
     * @throws [BadRequestException] if the provided [TaskStatus] is not valid.
     * @return A parsed Flow of all relevant tasks that meet the given timeframe.
     */
    override suspend fun findByPeriodFromResearcher(
        projectId: String,
        command: GetTaskCommand
    ): Flow<Map<String, Any?>> {
        if (command.status != null && !TaskStatus.values().any { it.name == command.status })
            throw BadRequestException("Invalid TaskStatus type: ${command.status}")

        return Authorizer.getAccount(AccessProjectAuthority(projectId))
            .flatMap {
                mono {
                    byCreatedAt(command)
                }
            }.awaitSingle()
    }

    /**
     * Returns relevant task results that have published time between the lastSyncTime and endTime specified by [GetTaskCommand].
     *
     * @param command [GetTaskCommand] with request specifications.
     * @throws [BadRequestException] if the provided [TaskStatus] is not valid.
     * @return A parsed Flow of all relevant tasks that meet the given timeframe.
     */
    override suspend fun findByPeriodFromParticipant(command: GetTaskCommand): Flow<Map<String, Any?>> {
        if (command.status != null && !TaskStatus.values().any { it.name == command.status })
            throw BadRequestException("Invalid TaskStatus type: ${command.status}")

        return byPublishedAt(command)
    }

    /**
     * Returns all tasks with relevant [createdAt][Task.createdAt] and [TaskStatus] values specified by [GetTaskCommand].
     *
     * If no startTime and endTime values are provided, they respectively default to provided values to limit the search range.
     *
     * @param command The [GetTaskCommand] with the user-provided specifications.
     * @return A parsed Flow of all relevant tasks created within the given timeframe.
     */
    private suspend fun byCreatedAt(command: GetTaskCommand): Flow<Map<String, Any?>> = convert(
        taskOutputPort.findByPeriod(
            command.startTime ?: LocalDateTime.parse("1900-01-01T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            command.endTime ?: LocalDateTime.parse("9999-12-31T23:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            command.status,
        )
    )

    /**
     * Returns all tasks with relevant [publishedAt][Task.publishedAt] values specified by [GetTaskCommand].
     *
     * @param command The [GetTaskCommand] with the user-provided specifications.
     * @throws [BadRequestException] if one or more of [lastSyncTime][GetTaskCommand.lastSyncTime] or [endTime][GetTaskCommand.endTime] values are not provided. These values cannot be null in a published Task instance.
     * @return A parsed Flow of all relevant tasks, last updated and completing within the given timeframe.
     */
    private suspend fun byPublishedAt(command: GetTaskCommand): Flow<Map<String, Any?>> =
        if (command.lastSyncTime == null || command.endTime == null)
            throw BadRequestException("You must provide end time.")
        else
            convert(taskOutputPort.findByPublishedAt(command.lastSyncTime, command.endTime))

    /**
     * Converts [Task] instances to a flattened Map.
     *
     * Allows users to easily view data associated with a Task instance.
     *
     * @param input Flow of Task instances to be parsed
     * @return Flow of tasks flattened as Map instances
     * @see Task.unrollTask
     */
    private suspend fun convert(input: Flow<Task>): Flow<Map<String, Any?>> =
        input.map { it.unrollTask() }
            .flatMapConcat {
                it["items"] = itemOutputPort.findByRevisionIdAndTaskId(it["revisionId"] as Int, it["id"].toString())
                    .map { item -> item.unrollItem() }.asFlux().collectList().awaitSingle()
                flowOf(it)
            }

    /**
     * Returns the task associated with the given id. Before that, checks that the user is authorized first.
     *
     * @param projectId The id of the project
     * @param taskId The id of the task
     * @return A parsed Flow of the task with the given id.
     */
    override suspend fun findById(projectId: String, taskId: String): Flow<Map<String, Any?>> {
        return Authorizer.getAccount(AccessProjectAuthority(projectId))
            .flatMap {
                mono {
                    taskOutputPort.findById(taskId)
                        .map { it.unrollTask() }
                        .flatMapConcat {
                            it["items"] = itemOutputPort
                                .findByRevisionIdAndTaskId(it["revisionId"] as Int, it["id"].toString())
                                .map { item -> item.unrollItem() }.asFlux().collectList().awaitSingle()
                            flowOf(it)
                        }
                }
            }.awaitSingle()
    }
}
