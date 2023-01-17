package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.application.port.input.CompletionTime
import com.samsung.healthcare.dataqueryservice.application.port.input.Task
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskItemResponse
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskResponseCount
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataResult
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.UUID

internal class TaskDataQueryServiceTest {
    private val queryDataPort = mockk<QueryDataPort>()

    private val taskDataQueryService = TaskDataQueryService(queryDataPort)

    private val testProjectId = "test-project-id"
    private val testAccountId = "test-account-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `listAllTasks should return all tasks`() {
        val tasks = listOf(
            Task(UUID.randomUUID().toString()),
            Task(UUID.randomUUID().toString())
        )

        every { queryDataPort.executeQuery(any(), any(), any()) } returns QueryDataResult(
            emptyList(),
            tasks.map { mapOf(TASK_ID_COLUMN to it.taskId) }
        )

        assertEquals(
            tasks,
            taskDataQueryService.listAllTasks(testProjectId, testAccountId)
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `responseCountOfAllTasks should return the number of responded user for each task`() {
        val responseCounts = listOf(
            TaskResponseCount(UUID.randomUUID().toString(), 7),
            TaskResponseCount(UUID.randomUUID().toString(), 123)
        )

        every { queryDataPort.executeQuery(any(), any(), any()) } returns QueryDataResult(
            emptyList(),
            responseCounts.map {
                mapOf(
                    TASK_ID_COLUMN to it.taskId,
                    NUMBER_OF_RESPONDED_USERS to it.count.toLong()
                )
            }
        )

        assertEquals(
            responseCounts,
            taskDataQueryService.responseCountOfAllTasks(testProjectId, testAccountId)
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `completionTimeOfAllTasks should average completion time for each task`() {
        val completionTimes = listOf(
            CompletionTime(UUID.randomUUID().toString(), 7.0),
            CompletionTime(UUID.randomUUID().toString(), 411.0)
        )

        every { queryDataPort.executeQuery(any(), any(), any()) } returns QueryDataResult(
            emptyList(),
            completionTimes.map {
                mapOf(
                    TASK_ID_COLUMN to it.taskId,
                    AVERAGE_COMPLETION_TIME_COLUMN to it.averageInMS
                )
            }
        )

        assertEquals(
            completionTimes,
            taskDataQueryService.completionTimeOfAllTasks(testProjectId, testAccountId)
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchTask should return given task`() {
        val task = Task(UUID.randomUUID().toString())
        every { queryDataPort.executeQuery(any(), any(), any(), any()) } returns QueryDataResult(
            emptyList(),
            listOf(mapOf(TASK_ID_COLUMN to task.taskId))
        )

        assertEquals(
            task,
            taskDataQueryService.fetchTask(testProjectId, task.taskId, testAccountId)
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `responseCountOfTask should return the number of responded user`() {
        val responseCount = TaskResponseCount(UUID.randomUUID().toString(), 141)

        every { queryDataPort.executeQuery(any(), any(), any(), any()) } returns QueryDataResult(
            emptyList(),
            listOf(
                mapOf(
                    TASK_ID_COLUMN to responseCount.taskId,
                    NUMBER_OF_RESPONDED_USERS to responseCount.count.toLong()
                )
            )
        )

        assertEquals(
            responseCount,
            taskDataQueryService.responseCountOfTask(testProjectId, responseCount.taskId, testAccountId)
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `completionTimeOfTask should average completion time `() {
        val completionTime =
            CompletionTime(UUID.randomUUID().toString(), 7.0)

        every { queryDataPort.executeQuery(any(), any(), any(), any()) } returns QueryDataResult(
            emptyList(),
            listOf(
                mapOf(
                    TASK_ID_COLUMN to completionTime.taskId,
                    AVERAGE_COMPLETION_TIME_COLUMN to completionTime.averageInMS
                )
            )
        )

        assertEquals(
            completionTime,
            taskDataQueryService.completionTimeOfTask(testProjectId, completionTime.taskId, testAccountId)
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchTaskItemResponse should average completion time `() {
        val taskItemResponses = listOf(
            TaskItemResponse("item-1", "user-x", "yes", emptyList()),
            TaskItemResponse("item-1", "user-y", "no", emptyList())
        )

        every { queryDataPort.executeQuery(any(), any(), any(), any()) } returns QueryDataResult(
            emptyList(),
            taskItemResponses.map {
                mapOf(
                    ITEM_NAME_COLUMN to it.itemName,
                    USER_ID_COLUMN to it.userId,
                    RESULT_COLUMN to it.result
                )
            }
        )
        assertEquals(
            taskItemResponses,
            taskDataQueryService.fetchTaskItemResponse(testProjectId, "taskId", emptyList(), testAccountId)
        )
    }
}
