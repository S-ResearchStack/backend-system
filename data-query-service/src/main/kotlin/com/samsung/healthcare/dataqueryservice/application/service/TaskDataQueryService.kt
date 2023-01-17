package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.application.port.input.Attribute
import com.samsung.healthcare.dataqueryservice.application.port.input.CompletionTime
import com.samsung.healthcare.dataqueryservice.application.port.input.Task
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskItemResponse
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskResponseCount
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import org.springframework.stereotype.Service

@Service
class TaskDataQueryService(
    private val queryDataPort: QueryDataPort,
) : TaskDataQuery {
    override fun listAllTasks(projectId: String, accountId: String): List<Task> {
        val result = queryDataPort.executeQuery(projectId, accountId, FIND_TASK_QUERY)
        return result.data.map {
            it.toTask()
        }
    }

    override fun responseCountOfAllTasks(projectId: String, accountId: String): List<TaskResponseCount> {
        val result = queryDataPort.executeQuery(
            projectId, accountId,
            """
                $TASK_RESPONSE_COUNT_QUERY
                GROUP BY $TASK_ID_COLUMN
            """
        )
        return result.data.map {
            it.toTaskResponseCount()
        }
    }

    override fun completionTimeOfAllTasks(projectId: String, accountId: String): List<CompletionTime> {
        val result = queryDataPort.executeQuery(
            projectId, accountId,
            """
                    $AVERAGE_COMPLETION_TIME_QUERY
                    GROUP BY $TASK_ID_COLUMN
                """
        )
        return result.data.map {
            it.toCompletionTime()
        }
    }

    override fun fetchTask(projectId: String, taskId: String, accountId: String): Task? {
        val result = queryDataPort.executeQuery(
            projectId, accountId,
            "$FIND_TASK_QUERY WHERE id = ?", listOf(taskId)
        )
        return result.data.firstOrNull()?.toTask()
    }

    override fun responseCountOfTask(projectId: String, taskId: String, accountId: String): TaskResponseCount? {
        val result = queryDataPort.executeQuery(
            projectId,
            accountId,
            """
                    $TASK_RESPONSE_COUNT_QUERY
                    WHERE $TASK_ID_COLUMN = ?
                    GROUP BY $TASK_ID_COLUMN
                """,
            listOf(taskId)
        )
        return result.data.firstOrNull()?.toTaskResponseCount()
    }

    override fun completionTimeOfTask(projectId: String, taskId: String, accountId: String): CompletionTime? {
        val result = queryDataPort.executeQuery(
            projectId,
            accountId,
            """
                    $AVERAGE_COMPLETION_TIME_QUERY
                    WHERE $TASK_ID_COLUMN = ?
                    GROUP BY $TASK_ID_COLUMN
                """,
            listOf(taskId)
        )
        return result.data.firstOrNull()?.toCompletionTime()
    }

    // FIXME change TaskItemResponse schema and query logic
    override fun fetchTaskItemResponse(
        projectId: String,
        taskId: String,
        includeAttributes: List<String>,
        accountId: String
    ): List<TaskItemResponse> {
        val profileSelect = if (includeAttributes.isNotEmpty())
            includeAttributes.joinToString(", ", prefix = ", ") {
                """json_extract_scalar(up.profile, '$.$it') as $it"""
            } else ""
        val result = queryDataPort.executeQuery(
            projectId,
            accountId,
            """
                    ${TASK_ITEM_RESPONSE_QUERY_FORMAT.format(profileSelect)}
                    WHERE ir.$TASK_ID_COLUMN = ?
                """,
            listOf(taskId)
        )

        return result.data.map { row ->
            val profiles = includeAttributes.map { Attribute(it, row[it]?.toString()) }
            TaskItemResponse(
                row[ITEM_NAME_COLUMN] as String,
                row[USER_ID_COLUMN] as String,
                row[RESULT_COLUMN] as String,
                profiles
            )
        }
    }

    private fun Map<String, Any?>.toTask() = Task(this[TASK_ID_COLUMN] as String)

    private fun Map<String, Any?>.toTaskResponseCount() =
        TaskResponseCount(
            this[TASK_ID_COLUMN] as String,
            (this[NUMBER_OF_RESPONDED_USERS] as Long).toInt()
        )

    private fun Map<String, Any?>.toCompletionTime() =
        CompletionTime(
            this[TASK_ID_COLUMN] as String,
            (this[AVERAGE_COMPLETION_TIME_COLUMN] as Double)
        )
}
