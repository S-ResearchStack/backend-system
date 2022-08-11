package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.platform.application.port.input.CreateProjectCommand
import com.samsung.healthcare.platform.application.port.output.CreateProjectPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import io.mockk.MockKMatcherScope
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CreateProjectServiceTest {
    private val createProjectPort = mockk<CreateProjectPort>()
    private val createProjectService = CreateProjectService(createProjectPort)

    @Test
    fun `should return new project id`() = runBlocking {
        val projectId = ProjectId.from(3)
        val createProjectCommand = CreateProjectCommand("project", mapOf("key" to "value"))

        coEvery {
            createProjectPort.create(isMatchProjectNameAndInfo(createProjectCommand))
        } returns projectId

        assertEquals(
            projectId,
            createProjectService.registerProject(createProjectCommand)
        )
    }

    private fun MockKMatcherScope.isMatchProjectNameAndInfo(createProjectCommand: CreateProjectCommand): Project =
        match { (_, name, info) ->
            name == createProjectCommand.name && info == createProjectCommand.info
        }
}
