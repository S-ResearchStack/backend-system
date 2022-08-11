package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.platform.application.port.input.CreateProjectCommand
import com.samsung.healthcare.platform.application.port.input.CreateProjectUseCase
import com.samsung.healthcare.platform.application.port.output.CreateProjectPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import org.springframework.stereotype.Service

@Service
class CreateProjectService(
    private val createProjectPort: CreateProjectPort
) : CreateProjectUseCase {
    override suspend fun registerProject(command: CreateProjectCommand): ProjectId =
        /* TODO check authorization
            [Alt 1] in Routing, allow if user is researcher
            [Alt 2] add UserDetail parameter in createProject method, and apply aop
         */
        createProjectPort.create(
            Project.newProject(command.name, command.info, command.isOpen)
        )
}
