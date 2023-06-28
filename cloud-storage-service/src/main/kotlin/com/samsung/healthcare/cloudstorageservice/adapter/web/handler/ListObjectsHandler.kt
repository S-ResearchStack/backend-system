package com.samsung.healthcare.cloudstorageservice.adapter.web.handler

import com.samsung.healthcare.cloudstorageservice.adapter.web.common.getProjectId
import com.samsung.healthcare.cloudstorageservice.application.exception.BadRequestException
import com.samsung.healthcare.cloudstorageservice.application.port.input.ListObjectsUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ListObjectsHandler(
    private val listObjectsUseCase: ListObjectsUseCase,
) {
    fun listObjects(req: ServerRequest): Mono<ServerResponse> {
        val path = req.queryParam("path").orElseThrow {
            throw BadRequestException("no path")
        }

        return listObjectsUseCase.listObjects(req.getProjectId(), path)
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
    }
}
