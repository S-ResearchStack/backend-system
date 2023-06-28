package com.samsung.healthcare.cloudstorageservice.application.port.input

import reactor.core.publisher.Mono
import java.net.URL

interface UploadObjectUseCase {
    fun getSignedUrl(projectId: String, objectName: String): Mono<URL>

    fun getParticipantSignedUrl(projectId: String, objectName: String): Mono<URL>
}
