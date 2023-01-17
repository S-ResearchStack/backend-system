package com.samsung.healthcare.dataqueryservice.application.port.input

data class Task(val taskId: String)

data class TaskResponseCount(
    val taskId: String,
    // TODO Long
    val count: Int
)

data class TaskItemResponse(
    val itemName: String,
    val userId: String,
    val result: String,
    val profiles: List<Attribute>,
)

data class CompletionTime(
    val taskId: String,
    val averageInMS: Double
)

interface TaskDataQuery {
    fun listAllTasks(projectId: String, accountId: String): List<Task>

    fun responseCountOfAllTasks(projectId: String, accountId: String): List<TaskResponseCount>

    fun completionTimeOfAllTasks(projectId: String, accountId: String): List<CompletionTime>

    fun fetchTask(projectId: String, taskId: String, accountId: String): Task?

    fun responseCountOfTask(projectId: String, taskId: String, accountId: String): TaskResponseCount?

    fun completionTimeOfTask(projectId: String, taskId: String, accountId: String): CompletionTime?

    fun fetchTaskItemResponse(
        projectId: String,
        taskId: String,
        includeAttributes: List<String>,
        accountId: String
    ): List<TaskItemResponse>
}
