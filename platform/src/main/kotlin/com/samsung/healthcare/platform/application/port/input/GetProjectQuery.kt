package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.flow.Flow

interface GetProjectQuery {
    suspend fun findProjectById(id: ProjectId): Project

    // TODO return project list which user can access to
    fun listProject(): Flow<Project>
}
