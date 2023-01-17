package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.output.project.task.TaskOutputPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.TaskStatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono

internal class CreateTaskServiceTest {
    private val taskOutputPort = mockk<TaskOutputPort>()
    private val createTaskService = CreateTaskService(
        taskOutputPort
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
        mockkObject(ContextHolder)
        val wrongProjectId = Project.ProjectId.from(2)
        every { ContextHolder.getAccount() } returns Mono.just(account)

        assertThrows<ForbiddenException>("should throw an forbidden exception") {
            createTaskService.createTask(wrongProjectId.toString())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should match properties of generated task`() = runTest {
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        val revisionId = RevisionId.from(1)
        val task = Task(
            revisionId,
            "task-id",
            emptyMap(),
            TaskStatus.DRAFT
        )
        coEvery {
            taskOutputPort.create(any())
        } returns task

        val response = createTaskService.createTask(projectId.toString())

        assertAll(
            "TaskResponse properties",
            { assertEquals(revisionId.value, response.revisionId) },
            { assertEquals(task.id, response.id) }
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should not allow null RevisionId`() = runTest {
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessProjectAuthority(projectId.toString())) } returns mono { account }

        val illegalTask = Task(
            null,
            "task-id",
            emptyMap(),
            TaskStatus.DRAFT
        )
        coEvery {
            taskOutputPort.create(any())
        } returns illegalTask

        assertThrows<java.lang.IllegalArgumentException>("should require not null") {
            createTaskService.createTask(projectId.toString())
        }
    }
}
