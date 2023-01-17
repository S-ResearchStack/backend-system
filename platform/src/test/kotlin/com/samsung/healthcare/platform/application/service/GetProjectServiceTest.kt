package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.output.LoadProjectPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDateTime

internal class GetProjectServiceTest {
    private val loadProjectPort = mockk<LoadProjectPort>()
    private val getProjectService = GetProjectService(loadProjectPort)

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return project if ProjectId exists`() = runTest {
        mockkObject(Authorizer)
        val projectId = ProjectId.from(1)
        val account = Account(
            "account-id",
            Email("cubist@test.com"),
            listOf(Researcher(projectId.value.toString()))
        )
        every { Authorizer.getAccount(AccessProjectAuthority("1")) } returns mono { account }
        coEvery { loadProjectPort.findById(projectId) } returns Project(
            projectId,
            "project",
            emptyMap(),
            true,
            LocalDateTime.now()
        )

        val project = getProjectService.findProjectById(projectId)

        assertNotNull(project)
        assertEquals(projectId, project.id)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw exception if project does not exist`() = runTest {
        mockkObject(Authorizer)
        val projectId = ProjectId.from(1)
        val account = Account(
            "account-id",
            Email("cubist@test.com"),
            listOf(Researcher(projectId.value.toString()))
        )
        every { Authorizer.getAccount(AccessProjectAuthority("1")) } returns mono { account }
        coEvery { loadProjectPort.findById(projectId) } returns null
        assertThrows<NotFoundException>("should throw an exception") {
            getProjectService.findProjectById(projectId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return empty if no accessible projects exist`() = runTest {
        mockkObject(Authorizer)
        val accessibleProjects = emptyList<ProjectId>().toMono()
        every { Authorizer.getAccessibleProjects() } returns accessibleProjects
        every { loadProjectPort.findProjectByIdIn(emptyList()) } returns emptyFlow()

        val projectFlow = getProjectService.listProject()
        assertEquals(0, projectFlow.count())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return all accessible projects`() = runTest {
        mockkObject(ContextHolder)
        val projectId1 = ProjectId.from(1)
        val projectId2 = ProjectId.from(2)
        val account = Account(
            "account-id",
            Email("cubist@test.com"),
            listOf(Researcher("1"), Researcher("2"))
        )
        every { ContextHolder.getAccount() } returns Mono.just(account)

        val project1 = Project(projectId1, "project 1", emptyMap(), true)
        val project2 = Project(projectId2, "project 2", emptyMap(), true)
        every { loadProjectPort.findProjectByIdIn(listOf(projectId1, projectId2)) } returns flowOf(project1, project2)

        val projectFlow = getProjectService.listProject()
        assertEquals(
            listOf(project1, project2),
            projectFlow.toList()
        )
    }
}
