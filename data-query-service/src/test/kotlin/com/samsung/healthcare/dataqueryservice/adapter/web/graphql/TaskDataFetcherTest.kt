package com.samsung.healthcare.dataqueryservice.adapter.web.graphql

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.types.errors.ErrorType.BAD_REQUEST
import com.netflix.graphql.types.errors.TypedGraphQLError
import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.adapter.web.PROJECT_ID
import com.samsung.healthcare.dataqueryservice.adapter.web.exception.DataFetchingExceptionHandler
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import com.samsung.healthcare.dataqueryservice.application.port.input.CompletionTime
import com.samsung.healthcare.dataqueryservice.application.port.input.Task
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.input.TaskResponseCount
import graphql.ExecutionResult
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import java.util.UUID
import kotlin.random.Random

@SpringBootTest(classes = [DgsAutoConfiguration::class, TaskDataFetcher::class, DataFetchingExceptionHandler::class])
internal class TaskDataFetcherTest {
    @MockkBean
    lateinit var taskQuery: TaskDataQuery

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    @Tag(POSITIVE_TEST)
    fun `taskResults should return all results when specific task id is not given`() {
        val tasks = listOf(
            Task(UUID.randomUUID().toString()),
            Task(UUID.randomUUID().toString())
        )
        val responseCounts = tasks.map { TaskResponseCount(it.taskId, Random.nextInt(0, 777)) }
        val completionTimes = tasks.map { CompletionTime(it.taskId, Random.nextDouble(0.0, 999.0)) }

        every { taskQuery.listAllTasks(any(), any()) } returns tasks
        every { taskQuery.responseCountOfAllTasks(any(), any()) } returns responseCounts
        every { taskQuery.completionTimeOfAllTasks(any(), any()) } returns completionTimes

        val result = executeTaskResultQuery()
        assertTrue(result.errors.isEmpty())

        val data = result.getData<Map<String, List<Map<String, Any>>>>()["taskResults"] ?: fail("require result data")
        assertEquals(tasks, data.map { Task(it["taskId"] as String) })
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `taskResults should return results of given task id`() {
        val task = Task(UUID.randomUUID().toString())

        val responseCount = TaskResponseCount(task.taskId, Random.nextInt(0, 777))
        val completionTime = CompletionTime(task.taskId, Random.nextDouble(0.0, 999.0))

        every { taskQuery.fetchTask(any(), task.taskId, any()) } returns task
        every { taskQuery.responseCountOfTask(any(), task.taskId, any()) } returns responseCount
        every { taskQuery.completionTimeOfTask(any(), task.taskId, any()) } returns completionTime

        val result = executeTaskResultQuery(task.taskId)
        assertTrue(result.errors.isEmpty())

        val data = result.getData<Map<String, List<Map<String, Any>>>>()["taskResults"] ?: fail("require result data")
        val actualTasks = data.map { Task(it["taskId"] as String) }
        assertEquals(1, actualTasks.size)
        assertEquals(task, actualTasks.first())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", "  "])
    fun `taskResults should contains bad-request error`(taskId: String) {
        val result = executeTaskResultQuery(taskId)
        assertTrue(result.errors.isNotEmpty())

        val errorTypes = result.errors.filterIsInstance<TypedGraphQLError>()
            .map { it.extensions["errorType"] as String }

        assertTrue(errorTypes.contains(BAD_REQUEST.name))
    }

    private fun executeTaskResultQuery(taskId: String? = null): ExecutionResult {
        val condition = taskId?.let { """(taskId:  "$it")""" } ?: ""
        return executeGraphQLQuery(
            """
                    {
                        taskResults$condition {
                            taskId
                            numberOfRespondedUser {
                                count
                            }
                            completionTime {
                                averageInMS
                            }
                        }
                    }
            """.trimIndent()
        )
    }

    private fun executeGraphQLQuery(query: String): ExecutionResult = dgsQueryExecutor.execute(
        query, emptyMap(), emptyMap(),
        HttpHeaders().apply {
            this[PROJECT_ID] = "test-project-id"
            this[AuthContext.ACCOUNT_ID_KEY_NAME] = "test-account-id"
        }
    )
}
