package com.samsung.healthcare.platform.adapter.web.filter

import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class TenantHandlerFilterFunction : HandlerFilterFunction<ServerResponse, ServerResponse> {
    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> =
        try {
            val projectId = request.pathVariable("projectId")

            next.handle(request)
                .contextWrite(ContextHolder.setProjectId(projectId))
        } catch (e: java.lang.IllegalArgumentException) {
            next.handle(request)
        }
}
