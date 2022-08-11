package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId

interface CreateProjectPort {
    suspend fun create(project: Project): ProjectId
}
