package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.account.domain.ReadStudyOverviewAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.application.port.output.LoadProjectPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GetProjectService(
    private val loadProjectPort: LoadProjectPort
) : GetProjectQuery {

    /**
     * Returns a [Project] associated with the given ProjectId.
     *
     * @param id ProjectId
     * @throws [NotFoundException] if no project with the given ProjectId exists.
     * @return [Project] with the corresponding ProjectId.
     */
    override suspend fun findProjectById(id: ProjectId): Project =
        Authorizer.getAccount(ReadStudyOverviewAuthority(id.value.toString()))
            .flatMap {
                mono {
                    loadProjectPort.findById(id)
                }
            }.switchIfEmpty(Mono.error(NotFoundException()))
            .awaitSingle()

    /**
     * List all projects that the requesting account has access to.
     *
     * @return A Flow of [Project]s that the requesting account can access. Returns an empty Flow if no such Projects exist.
     */
    override fun listProject(): Flow<Project> =
        Authorizer.getAccessibleProjects()
            .flatMapMany { projectIds ->
                loadProjectPort.findProjectByIdIn(projectIds)
                    .asFlux()
            }.asFlow()

    /**
     * Check a project associated with the given ProjectId.
     *
     * @param id ProjectId
     * @return [Boolean]
     */
    override suspend fun existsProject(id: ProjectId): Boolean =
        loadProjectPort.existsById(id)
}
