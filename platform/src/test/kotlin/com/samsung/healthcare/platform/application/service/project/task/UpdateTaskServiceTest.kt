package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskCommand
import com.samsung.healthcare.platform.application.port.output.project.task.ItemOutputPort
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.project.task.Item
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.ItemType
import com.samsung.healthcare.platform.enums.TaskStatus
import com.samsung.healthcare.platform.enums.TaskType
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class UpdateTaskServiceTest {
    private val taskOutputPort = mockk<TaskOutputPort>()
    private val itemOutputPort = mockk<ItemOutputPort>()
    private val updateTaskService = UpdateTaskService(
        taskOutputPort,
        itemOutputPort
    )

    private val projectId = Project.ProjectId.from(1)
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.Researcher(projectId.value.toString()))
    )

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw forbidden when account do not have project authority`() = runTest {
        mockkObject(Authorizer)
        val wrongProjectId = Project.ProjectId.from(2)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        assertThrows<ForbiddenException>("should throw an forbidden exception") {
            updateTaskService.updateTask(
                wrongProjectId.toString(),
                "test-task",
                RevisionId.from(1),
                UpdateTaskCommand(
                    title = "title",
                    status = TaskStatus.DRAFT,
                    type = TaskType.SURVEY,
                    items = emptyList()
                ),
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should not update publishedAt`() = runTest {
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        val taskId = "test-task"
        val revisionId = RevisionId.from(1)
        val updateTaskCommand = UpdateTaskCommand(
            title = "not yet published",
            description = "updating without publishing",
            status = TaskStatus.DRAFT,
            type = TaskType.SURVEY,
            items = emptyList()
        )
        val task = Task(
            revisionId,
            taskId,
            updateTaskCommand.properties,
            updateTaskCommand.status,
            updateTaskCommand.type
        )

        coEvery { taskOutputPort.findByIdAndRevisionId(taskId, revisionId) } returns task
        coEvery { taskOutputPort.findById(taskId) } returns flowOf(task)
        coEvery { taskOutputPort.update(task) } returns task
        coJustRun { itemOutputPort.update(revisionId.value, emptyList()) }

        updateTaskService.updateTask(
            projectId.toString(),
            taskId,
            revisionId,
            updateTaskCommand
        )

        coVerify { taskOutputPort.update(task) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should default endTime to 3mo after startTime if no value provided`() = runTest {
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        val taskId = "test-task"
        val revisionId = RevisionId.from(1)
        val contentsSample = mapOf(
            "title" to "myTitle",
            "type" to "CHOICE",
            "properties" to mapOf(
                "tag" to "RADIO",
                "options" to listOf(
                    mapOf("value" to "value")
                )
            )
        )
        val updateTaskCommand = UpdateTaskCommand(
            title = "endTime test",
            description = "should default to 3 months later",
            schedule = "0 0 0/1 1/1 * ? *",
            startTime = LocalDateTime.parse("2022-01-20T10:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            validTime = 12,
            status = TaskStatus.PUBLISHED,
            type = TaskType.SURVEY,
            items = listOf(
                UpdateTaskCommand.UpdateItemCommand(contentsSample, ItemType.QUESTION, 0),
                UpdateTaskCommand.UpdateItemCommand(contentsSample, ItemType.QUESTION, 1)
            )
        )
        mockkStatic(LocalDateTime::class)
        val testLocalDateTime = LocalDateTime.parse("2022-10-21T17:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        every { LocalDateTime.now() } returns testLocalDateTime

        val returnedTask = Task(
            revisionId,
            taskId,
            updateTaskCommand.properties,
            TaskStatus.DRAFT,
            TaskType.SURVEY
        )

        val task = Task(
            revisionId,
            taskId,
            updateTaskCommand.properties,
            updateTaskCommand.status,
            updateTaskCommand.type,
            publishedAt = testLocalDateTime
        )

        val item1 = Item(
            null,
            revisionId,
            taskId,
            "Question0",
            contentsSample,
            ItemType.QUESTION,
            0
        )
        val item2 = Item(
            null,
            revisionId,
            taskId,
            "Question1",
            contentsSample,
            ItemType.QUESTION,
            1
        )

        coEvery { taskOutputPort.findByIdAndRevisionId(taskId, revisionId) } returns returnedTask
        coEvery { taskOutputPort.findById(taskId) } returns flowOf(returnedTask)
        coEvery { taskOutputPort.update(task) } returns task
        coJustRun { itemOutputPort.update(revisionId.value, listOf(item1, item2)) }

        updateTaskService.updateTask(projectId.toString(), taskId, revisionId, updateTaskCommand)

        coVerifyOrder {
            taskOutputPort.update(task)
            itemOutputPort.update(revisionId.value, listOf(item1, item2))
        }
    }
}
