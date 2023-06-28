package com.samsung.healthcare.cloudstorageservice.application.service

import com.samsung.healthcare.account.domain.AccessDocumentAuthority
import com.samsung.healthcare.cloudstorageservice.application.authorize.Authorizer
import com.samsung.healthcare.cloudstorageservice.application.config.SignedUrlProperties
import com.samsung.healthcare.cloudstorageservice.application.port.input.DeleteObjectUseCase
import com.samsung.healthcare.cloudstorageservice.application.port.output.DeleteObjectPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.net.URL

@Service
class DeleteObjectService(
    private val signedUrlProperties: SignedUrlProperties,
    private val deleteObjectPort: DeleteObjectPort,
) : DeleteObjectUseCase {
    override fun getSignedUrl(projectId: String, objectName: String): Mono<URL> {
        require(projectId.isNotBlank()) { "projectId was empty" }
        require(objectName.isNotBlank()) { "objectName was empty" }

        return Authorizer.getAccount(AccessDocumentAuthority(projectId))
            .map {
                deleteObjectPort.getDeleteSignedUrl("$projectId/$objectName", signedUrlProperties.duration)
            }
    }
}
