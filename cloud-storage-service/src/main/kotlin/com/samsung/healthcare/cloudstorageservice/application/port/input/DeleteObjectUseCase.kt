package com.samsung.healthcare.cloudstorageservice.application.port.input

import reactor.core.publisher.Mono
import java.net.URL

interface DeleteObjectUseCase {
    fun getSignedUrl(projectId: String, objectName: String): Mono<URL>
}
