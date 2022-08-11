package com.samsung.healthcare.platform.adapter.web

import com.samsung.healthcare.platform.application.port.input.CreateProjectUseCase
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.net.URI

@Component
class ProjectHandler(
    private val createProjectUseCase: CreateProjectUseCase,
    private val getProjectQuery: GetProjectQuery
) {
    suspend fun createProject(req: ServerRequest): ServerResponse {
        val projectId = createProjectUseCase.registerProject(
            req.awaitBody()
        )

        return ServerResponse.created(URI.create("/api/projects/$projectId"))
            .buildAndAwait()
    }

    suspend fun findProjectById(req: ServerRequest): ServerResponse {
        val project = getProjectQuery.findProjectById(
            ProjectId.from(req.getProjectId().toIntOrNull())
        )

        return ServerResponse.ok().bodyValue(project).awaitSingle()
    }

    suspend fun listProjects(req: ServerRequest): ServerResponse =
        ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(getProjectQuery.listProject())
}
