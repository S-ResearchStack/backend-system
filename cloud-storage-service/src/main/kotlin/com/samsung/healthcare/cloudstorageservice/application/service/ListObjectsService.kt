package com.samsung.healthcare.cloudstorageservice.application.service

import com.samsung.healthcare.account.domain.AccessDocumentAuthority
import com.samsung.healthcare.cloudstorageservice.application.authorize.Authorizer
import com.samsung.healthcare.cloudstorageservice.application.port.input.ListObjectsUseCase
import com.samsung.healthcare.cloudstorageservice.application.port.output.ListObjectsPort
import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ListObjectsService(
    private val listObjectsPort: ListObjectsPort,
) : ListObjectsUseCase {
    override fun listObjects(projectId: String, path: String): Mono<List<ObjectInfo>> {
        require(projectId.isNotBlank()) { "projectId was empty" }
        require(path.isNotBlank()) { "path was empty" }

        return Authorizer.getAccount(AccessDocumentAuthority(projectId))
            .map {
                listObjectsPort.list("$projectId/$path/")
                    .map {
                        ObjectInfo(
                            Regex("^$projectId/").replace(it.name, ""),
                            it.size
                        )
                    }
            }
    }
}
