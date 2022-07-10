package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.output.LoadProjectPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

internal class GetProjectServiceTest {
    private val loadProjectPort = mockk<LoadProjectPort>()
    private val getProjectService = GetProjectService(loadProjectPort)

    @Test
    fun `should return project if project id is existed`() = runBlocking {
        val projectId = ProjectId.from(1)
        coEvery { loadProjectPort.findById(projectId) } returns Project(
            projectId,
            "project",
            emptyMap(),
            true,
            LocalDateTime.now()
        )

        val account = Account(
            "account-id",
            Email("cubist@test.com"),
            listOf(Researcher(projectId.value.toString()))
        )
        val project = ContextHolder.setAccount(mono { getProjectService.findProjectById(projectId) }, account)
            .awaitSingle()

        assertNotNull(project)
        assertEquals(projectId, project.id)
    }

    @Test
    fun `should throw exception if existed project`() = runBlocking {
        val projectId = ProjectId.from(1)
        coEvery { loadProjectPort.findById(projectId) } returns null
        assertThrows<NotFoundException>("should throw an exception") {
            getProjectService.findProjectById(projectId)
        }
    }
}
