package com.samsung.healthcare.platform.adapter.web

import com.samsung.healthcare.platform.adapter.web.common.getProjectId
import com.samsung.healthcare.platform.application.port.input.CreateProjectUseCase
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.net.URI

@Component
class ProjectHandler(
    private val createProjectUseCase: CreateProjectUseCase,
    private val getProjectQuery: GetProjectQuery
) {

    /**
     * Handles requests to create new [Projects][com.samsung.healthcare.platform.domain.Project].
     *
     * @param req ServerRequest providing [CreateProjectCommand][com.samsung.healthcare.platform.application.port.input.CreateProjectCommand]
     * @return ServerResponse indicating that the project was created and can be accessed at the given URI.
     */
    suspend fun createProject(req: ServerRequest): ServerResponse {
        val projectId = createProjectUseCase.registerProject(
            req.awaitBody()
        )

        return ServerResponse.created(URI.create("/api/projects/$projectId"))
            .buildAndAwait()
    }

    /**
     * Handles requests to find [Projects][com.samsung.healthcare.platform.domain.Project] by id.
     *
     * @param req ServerRequest providing the project id in question.
     * @return ServerResponse with the relevant project data as its body.
     */
    suspend fun findProjectById(req: ServerRequest): ServerResponse {
        val project = getProjectQuery.findProjectById(
            ProjectId.from(req.getProjectId().toIntOrNull())
        )

        return ServerResponse.ok().bodyValue(project).awaitSingle()
    }

    /**
     * Handles requests to list all [Projects][com.samsung.healthcare.platform.domain.Project] the requesting account has access to.
     *
     * @param req ServerRequest with user context
     * @return ServerResponse with a Flow of relevant projects. Empty if no such projects exist.
     */
    suspend fun listProjects(req: ServerRequest): ServerResponse =
        ServerResponse.ok()
            .bodyValueAndAwait(
                getProjectQuery.listProject()
                    .asFlux()
                    .collectList()
                    .awaitSingle()
            )
}
