package com.samsung.healthcare.platform.adapter.web.filter

import com.samsung.healthcare.platform.adapter.web.common.getProjectId
import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class TenantHandlerFilterFunction(
    private val getProjectQuery: GetProjectQuery
) : HandlerFilterFunction<ServerResponse, ServerResponse> {
    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        val idResult = kotlin.runCatching { request.getProjectId() }
        // NOTE this is not request for project sub-path -> no need to write project id in context
        val projectIdString = idResult.getOrNull() ?: return next.handle(request)
        val projectId = ProjectId.from(projectIdString.toIntOrNull())
        return mono {
            getProjectQuery.existsProject(projectId)
        }.flatMap { exists ->
            if (!exists) return@flatMap Mono.error(NotFoundException())
            next.handle(request)
                .contextWrite(ContextHolder.setProjectId(projectIdString))
        }
    }
}
