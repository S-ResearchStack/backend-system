package com.samsung.healthcare.cloudstorageservice.application.port.output

import reactor.core.publisher.Mono

interface ExistsUserPort {
    fun exists(userId: String, projectId: String): Mono<Void>
}
