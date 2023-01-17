package com.samsung.healthcare.dataqueryservice.adapter.web.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.context.DgsContext
import com.samsung.healthcare.dataqueryservice.adapter.web.graphql.DgsContextExtension.getAccount
import com.samsung.healthcare.dataqueryservice.adapter.web.graphql.DgsContextExtension.getProjectId
import com.samsung.healthcare.dataqueryservice.application.port.input.CompletionTime
import com.samsung.healthcare.dataqueryservice.application.port.input.Task
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskItemResponse
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskResponseCount
import graphql.execution.DataFetcherResult
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class TaskDataFetcher(
    private val taskQuery: TaskDataQuery
) {

    @DgsQuery
    fun taskResults(
        dfe: DataFetchingEnvironment,
        @InputArgument taskId: String?
    ): DataFetcherResult<List<Task>> {
        if (taskId == null) {
            return fetchAllTaskResults(dfe)
        }
        return fetchTaskResult(dfe, taskId)
    }

    private fun fetchAllTaskResults(dfe: DataFetchingEnvironment): DataFetcherResult<List<Task>> {
        val context = DgsContext.from(dfe)
        val taskResults = taskQuery.listAllTasks(context.getProjectId(), context.getAccount())
        val result = DataFetcherResult.newResult<List<Task>>()
            .data(taskResults)
        val taskResultContextBuilder = TaskResultContext.Builder()
        if (dfe.selectionSet.contains("numberOfRespondedUser")) {
            val taskToResponseCount =
                taskQuery.responseCountOfAllTasks(context.getProjectId(), context.getAccount())
                    .associateBy { it.taskId }
            taskResultContextBuilder.taskToResponseCount = taskToResponseCount
        }
        if (dfe.selectionSet.contains("completionTime")) {
            val taskToCompletionTime =
                taskQuery.completionTimeOfAllTasks(context.getProjectId(), context.getAccount())
                    .associateBy { it.taskId }
            taskResultContextBuilder.taskToCompletionTime = taskToCompletionTime
        }
        return result.localContext(taskResultContextBuilder.build())
            .build()
    }

    private fun fetchTaskResult(dfe: DataFetchingEnvironment, taskId: String): DataFetcherResult<List<Task>> {
        require(taskId.isNotBlank())

        val context = DgsContext.from(dfe)
        val task = taskQuery.fetchTask(context.getProjectId(), taskId, context.getAccount())
            ?: return DataFetcherResult.newResult<List<Task>>().build()

        val result = DataFetcherResult.newResult<List<Task>>()
            .data(listOf(task))

        val taskResultContextBuilder = TaskResultContext.Builder()
        if (dfe.selectionSet.contains("numberOfRespondedUser")) {
            taskQuery.responseCountOfTask(context.getProjectId(), taskId, context.getAccount())
                ?.let {
                    taskResultContextBuilder.taskToResponseCount = mapOf(taskId to it)
                }
        }
        if (dfe.selectionSet.contains("completionTime")) {
            taskQuery.completionTimeOfTask(context.getProjectId(), taskId, context.getAccount())
                ?.let {
                    taskResultContextBuilder.taskToCompletionTime = mapOf(taskId to it)
                }
        }

        return result.localContext(taskResultContextBuilder.build())
            .build()
    }

    @DgsData(parentType = "TaskResult", field = "numberOfRespondedUser")
    fun numberOfRespondedUser(dfe: DgsDataFetchingEnvironment): TaskResponseCount {
        val taskResult = dfe.getSource<Task>()
        return dfe.getLocalContext<TaskResultContext>()
            .taskToResponseCount
            .getOrElse(taskResult.taskId) { TaskResponseCount(taskResult.taskId, 0) }
    }

    @DgsData(parentType = "TaskResult", field = "completionTime")
    fun completionTimeOfTask(dfe: DgsDataFetchingEnvironment): CompletionTime {
        val taskResult = dfe.getSource<Task>()
        return dfe.getLocalContext<TaskResultContext>()
            .taskToCompletionTime
            .getOrElse(taskResult.taskId) { CompletionTime(taskResult.taskId, 0.0) }
    }

    @DgsQuery
    fun surveyResponse(
        dfe: DataFetchingEnvironment,
        @InputArgument taskId: String,
        @InputArgument includeAttributes: List<String>?
    ): List<TaskItemResponse> {
        require(taskId.isNotBlank())

        val context = DgsContext.from(dfe)
        return taskQuery.fetchTaskItemResponse(
            context.getProjectId(),
            taskId,
            includeAttributes ?: emptyList(),
            context.getAccount()
        )
    }

    private data class TaskResultContext private constructor(
        val taskToResponseCount: Map<String, TaskResponseCount>,
        val taskToCompletionTime: Map<String, CompletionTime>
    ) {
        data class Builder(
            var taskToResponseCount: Map<String, TaskResponseCount> = emptyMap(),
            var taskToCompletionTime: Map<String, CompletionTime> = emptyMap()
        ) {
            fun withTaskToResponseCount(responseCount: Map<String, TaskResponseCount>) =
                apply { this.taskToResponseCount = responseCount }

            fun withtaskToCompletionTime(completionTime: Map<String, CompletionTime>) =
                apply { this.taskToCompletionTime = completionTime }

            fun build() = TaskResultContext(taskToResponseCount, taskToCompletionTime)
        }
    }
}
