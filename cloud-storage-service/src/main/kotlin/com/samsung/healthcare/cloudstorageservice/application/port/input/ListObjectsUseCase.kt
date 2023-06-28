package com.samsung.healthcare.cloudstorageservice.application.port.input

import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo
import reactor.core.publisher.Mono

interface ListObjectsUseCase {
    fun listObjects(projectId: String, path: String): Mono<List<ObjectInfo>>
}
