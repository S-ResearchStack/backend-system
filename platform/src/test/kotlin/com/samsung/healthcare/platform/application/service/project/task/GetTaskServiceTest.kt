package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.BadRequestException
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskCommand
import com.samsung.healthcare.platform.application.port.output.project.task.ItemOutputPort
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.project.task.Item
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.ItemType
import com.samsung.healthcare.platform.enums.TaskStatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class GetTaskServiceTest {
    private val taskOutputPort = mockk<TaskOutputPort>()
    private val itemOutputPort = mockk<ItemOutputPort>()
    private val getTaskService = GetTaskService(
        taskOutputPort,
        itemOutputPort
    )

    private val projectId = Project.ProjectId.from(1)
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.Researcher(projectId.value.toString()))
    )

    // Test Tasks
    private val task1 = Task(
        RevisionId.from(1),
        "test-task1",
        emptyMap(),
        TaskStatus.PUBLISHED,
        createdAt = LocalDateTime.parse("1999-12-31T23:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        publishedAt = LocalDateTime.parse("2022-10-01T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
    private val task2 = Task(
        RevisionId.from(2),
        "test-task2",
        emptyMap(),
        TaskStatus.PUBLISHED,
        createdAt = LocalDateTime.parse("2022-01-20T10:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        publishedAt = LocalDateTime.parse("2022-08-20T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
    private val task3 = Task(
        RevisionId.from(3),
        "test-task3",
        mapOf(
            "title" to "Task 3",
            "description" to "For testing, has items"
        ),
        TaskStatus.PUBLISHED,
        createdAt = LocalDateTime.parse("2022-03-01T07:50", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        publishedAt = LocalDateTime.parse("2022-10-19T11:52", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )

    // Test Items
    private val item1 = Item(
        0,
        RevisionId.from(3),
        "test-task3",
        "Question 1",
        mapOf("title" to "Q1"),
        ItemType.QUESTION,
        0
    )
    private val item2 = Item(
        1,
        RevisionId.from(3),
        "test-task3",
        "Question 2",
        mapOf(
            "title" to "Q2",
            "explanation" to "text here"
        ),
        ItemType.QUESTION,
        1
    )

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriodFromResearcher should throw exception if TaskStatus invalid`() = runTest {
        val getTaskCommand = GetTaskCommand(null, null, null, "invalid")

        assertThrows<BadRequestException>("should require valid status") {
            getTaskService.findByPeriodFromResearcher(projectId.toString(), getTaskCommand)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriodFromParticipant should throw forbidden when account do not have project authority`() = runTest {
        mockkObject(ContextHolder)
        every { ContextHolder.getAccount() } returns Mono.just(account)

        val wrongProjectId = Project.ProjectId.from(2)
        val endTime = LocalDateTime.parse("2022-02-24T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val getTaskCommand = GetTaskCommand(null, endTime, null, "PUBLISHED")

        assertThrows<ForbiddenException>("should throw an forbidden exception") {
            getTaskService.findByPeriodFromResearcher(wrongProjectId.toString(), getTaskCommand)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findByPeriodFromResearcher should work properly`() = runTest {
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        val endTime = LocalDateTime.parse("2022-02-24T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val getTaskCommand = GetTaskCommand(null, endTime, null, "PUBLISHED")

        coEvery {
            taskOutputPort.findByPeriod(
                LocalDateTime.parse("1900-01-01T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                endTime,
                "PUBLISHED"
            )
        } returns flowOf(task1, task2)

        coEvery {
            itemOutputPort.findByRevisionIdAndTaskId(1, "test-task1")
        } returns emptyFlow()

        coEvery {
            itemOutputPort.findByRevisionIdAndTaskId(2, "test-task2")
        } returns emptyFlow()

        val result = getTaskService.findByPeriodFromResearcher(projectId.toString(), getTaskCommand)

        assertEquals(
            listOf(flatten(task1, emptyList()), flatten(task2, emptyList())),
            result.toList()
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriodFromParticipant should throw exception if TaskStatus invalid`() = runTest {
        val getTaskCommand = GetTaskCommand(null, null, null, "invalid")

        assertThrows<BadRequestException>("should require valid status") {
            getTaskService.findByPeriodFromParticipant(getTaskCommand)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriodFromParticipant should throw exception if range not given_byPublishedAt`() = runTest {
        assertThrows<BadRequestException>("should require endTime") {
            getTaskService.findByPeriodFromParticipant(
                GetTaskCommand(null, null, LocalDateTime.now(), "PUBLISHED")
            )
        }

        assertThrows<BadRequestException>("should require lastSyncTime") {
            getTaskService.findByPeriodFromParticipant(
                GetTaskCommand(null, LocalDateTime.now(), null, "PUBLISHED")
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findByPeriodFromParticipant should work properly`() = runTest {
        val lastSyncTime = LocalDateTime.parse("2022-09-30T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val endTime = LocalDateTime.parse("2022-10-21T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val getTaskCommand = GetTaskCommand(null, endTime, lastSyncTime, null)

        coEvery {
            taskOutputPort.findByPublishedAt(
                lastSyncTime,
                endTime
            )
        } returns flowOf(task1, task3)

        coEvery {
            itemOutputPort.findByRevisionIdAndTaskId(1, "test-task1")
        } returns emptyFlow()

        coEvery {
            itemOutputPort.findByRevisionIdAndTaskId(3, "test-task3")
        } returns flowOf(item1, item2)

        val result = getTaskService.findByPeriodFromParticipant(getTaskCommand)

        assertEquals(
            listOf(flatten(task1, emptyList()), flatten(task3, listOf(item1, item2))),
            result.toList()
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findById should return empty Flow if Task with id does not exist`() = runTest {
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        coEvery {
            taskOutputPort.findById(any())
        } returns emptyFlow()

        val result = getTaskService.findById(projectId.toString(), "nonexistent-id")

        assertEquals(
            0,
            result.count()
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findById should return Task with matching id`() = runTest {
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        val taskId = "test-id"
        val revisionId = RevisionId.from(1)
        val properties = mapOf(
            "title" to "test task",
            "description" to "test description"
        )
        val task = Task(
            revisionId,
            taskId,
            properties,
            TaskStatus.DRAFT
        )

        coEvery {
            taskOutputPort.findById(taskId)
        } returns flowOf(task)

        coEvery {
            itemOutputPort.findByRevisionIdAndTaskId(revisionId.value, taskId)
        } returns emptyFlow()

        val result = getTaskService.findById(projectId.toString(), taskId)

        assertEquals(
            listOf(flatten(task, emptyList())),
            result.toList()
        )
    }

    private fun flatten(task: Task, items: List<Item>): Map<String, Any?> {
        val itemList: List<Map<String, Any?>> = items.map { it.unrollItem() }
        return task.unrollTask() + mapOf("items" to itemList)
    }
}
