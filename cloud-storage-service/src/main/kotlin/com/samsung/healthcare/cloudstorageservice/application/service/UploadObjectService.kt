package com.samsung.healthcare.cloudstorageservice.application.service

import com.samsung.healthcare.account.domain.AccessDocumentAuthority
import com.samsung.healthcare.cloudstorageservice.application.authorize.Authorizer
import com.samsung.healthcare.cloudstorageservice.application.config.SignedUrlProperties
import com.samsung.healthcare.cloudstorageservice.application.port.input.UploadObjectUseCase
import com.samsung.healthcare.cloudstorageservice.application.port.output.UploadObjectPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.URL

@Service
class UploadObjectService(
    private val signedUrlProperties: SignedUrlProperties,
    private val uploadObjectPort: UploadObjectPort,
) : UploadObjectUseCase {
    override fun getSignedUrl(projectId: String, objectName: String): Mono<URL> {
        require(projectId.isNotBlank()) { "projectId was empty" }
        require(objectName.isNotBlank()) { "objectName was empty" }

        return Authorizer.getAccount(AccessDocumentAuthority(projectId))
            .map {
                uploadObjectPort.getUploadSignedUrl("$projectId/$objectName", signedUrlProperties.duration)
            }
    }

    override fun getParticipantSignedUrl(projectId: String, objectName: String): Mono<URL> {
        require(projectId.isNotBlank()) { "projectId was empty" }
        require(objectName.isNotBlank()) { "objectName was empty" }

        return uploadObjectPort.getUploadSignedUrl("$projectId/$objectName", signedUrlProperties.duration).toMono()
    }
}
