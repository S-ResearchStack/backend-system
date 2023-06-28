package com.samsung.healthcare.cloudstorageservice.application.port.input

import reactor.core.publisher.Mono
import java.net.URL

interface DownloadObjectUseCase {
    fun getSignedUrl(projectId: String, objectName: String, urlDuration: Long? = null): Mono<URL>

    fun getParticipantSignedUrl(projectId: String, objectName: String): Mono<URL>
}
