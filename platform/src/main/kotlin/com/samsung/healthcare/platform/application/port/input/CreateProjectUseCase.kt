package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.Project.ProjectId

interface CreateProjectUseCase {
    suspend fun registerProject(command: CreateProjectCommand): ProjectId
}
