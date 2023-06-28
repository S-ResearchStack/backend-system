package com.samsung.healthcare.cloudstorageservice.adapter.web.handler

import com.samsung.healthcare.cloudstorageservice.adapter.web.common.getProjectId
import com.samsung.healthcare.cloudstorageservice.application.exception.BadRequestException
import com.samsung.healthcare.cloudstorageservice.application.port.input.DownloadObjectUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class DownloadObjectHandler(
    private val downloadObjectUseCase: DownloadObjectUseCase,
) {
    fun download(req: ServerRequest): Mono<ServerResponse> {
        val objectName = req.queryParam("object_name").orElseThrow {
            throw BadRequestException("no object_name")
        }

        return downloadObjectUseCase.getSignedUrl(req.getProjectId(), objectName)
            .flatMap {
                ServerResponse.temporaryRedirect(it.toURI()).build()
            }
    }

    fun getDownloadUrl(req: ServerRequest): Mono<ServerResponse> {
        val objectName = req.queryParam("object_name").orElseThrow {
            throw BadRequestException("no object_name")
        }
        val urlDuration: Long? = try {
            req.queryParam("url_duration").map {
                it.toLong()
            }.orElse(null)
        } catch (_: NumberFormatException) {
            throw IllegalArgumentException("url_duration format error")
        }

        return downloadObjectUseCase.getSignedUrl(req.getProjectId(), objectName, urlDuration)
            .flatMap {
                ServerResponse.ok().bodyValue(it.toString())
            }
    }

    fun participantDownload(req: ServerRequest): Mono<ServerResponse> {
        val objectName = req.queryParam("object_name").orElseThrow {
            throw BadRequestException("no object_name")
        }

        return downloadObjectUseCase.getParticipantSignedUrl(req.getProjectId(), objectName)
            .flatMap {
                ServerResponse.temporaryRedirect(it.toURI()).build()
            }
    }
}
