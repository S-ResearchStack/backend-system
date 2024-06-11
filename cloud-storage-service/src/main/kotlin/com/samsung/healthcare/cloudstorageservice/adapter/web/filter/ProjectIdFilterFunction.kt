package com.samsung.healthcare.cloudstorageservice.adapter.web.filter

import com.samsung.healthcare.cloudstorageservice.adapter.web.common.getProjectId
import com.samsung.healthcare.cloudstorageservice.application.exception.BadRequestException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProjectIdFilterFunction : HandlerFilterFunction<ServerResponse, ServerResponse> {
    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        val idResult = kotlin.runCatching { request.getProjectId() }
        idResult.getOrNull()?.toIntOrNull() ?: throw BadRequestException()
        return next.handle(request)
    }
}
