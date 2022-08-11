package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.flow.Flow

interface LoadProjectPort {
    suspend fun findById(id: ProjectId): Project?

    fun findAll(): Flow<Project>
}
