package com.samsung.healthcare.cloudstorageservice.adapter.web.handler

import com.samsung.healthcare.cloudstorageservice.adapter.web.common.getProjectId
import com.samsung.healthcare.cloudstorageservice.application.exception.BadRequestException
import com.samsung.healthcare.cloudstorageservice.application.port.input.DeleteObjectUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class DeleteObjectHandler(
    private val deleteObjectUseCase: DeleteObjectUseCase,
) {
    fun delete(req: ServerRequest): Mono<ServerResponse> {
        val objectName = req.queryParam("object_name").orElseThrow {
            throw BadRequestException("no object_name")
        }

        return deleteObjectUseCase.getSignedUrl(req.getProjectId(), objectName)
            .flatMap {
                ServerResponse.temporaryRedirect(it.toURI()).build()
            }
    }
}
