package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.application.port.output.LoadProjectPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class GetProjectService(
    private val loadProjectPort: LoadProjectPort
) : GetProjectQuery {
    override suspend fun findProjectById(id: ProjectId): Project =
        loadProjectPort.findById(id) ?: throw NotFoundException("The project($id) does not exist.")

    override fun listProject(): Flow<Project> =
        loadProjectPort.findAll()
}
