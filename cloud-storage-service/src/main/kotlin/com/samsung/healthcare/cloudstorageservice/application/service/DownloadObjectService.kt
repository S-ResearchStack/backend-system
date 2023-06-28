package com.samsung.healthcare.cloudstorageservice.application.service

import com.samsung.healthcare.account.domain.AccessDocumentAuthority
import com.samsung.healthcare.cloudstorageservice.application.authorize.Authorizer
import com.samsung.healthcare.cloudstorageservice.application.config.SignedUrlProperties
import com.samsung.healthcare.cloudstorageservice.application.port.input.DownloadObjectUseCase
import com.samsung.healthcare.cloudstorageservice.application.port.output.DownloadObjectPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.net.URL
import reactor.kotlin.core.publisher.toMono

@Service
class DownloadObjectService(
    private val signedUrlProperties: SignedUrlProperties,
    private val downloadObjectPort: DownloadObjectPort,
) : DownloadObjectUseCase {
    override fun getSignedUrl(projectId: String, objectName: String, urlDuration: Long?): Mono<URL> {
        require(projectId.isNotBlank()) { "projectId was empty" }
        require(objectName.isNotBlank()) { "objectName was empty" }

        return Authorizer.getAccount(AccessDocumentAuthority(projectId))
            .map {
                downloadObjectPort.getDownloadSignedUrl(
                    "$projectId/$objectName", urlDuration ?: signedUrlProperties.duration
                )
            }
    }

    override fun getParticipantSignedUrl(projectId: String, objectName: String): Mono<URL> {
        require(projectId.isNotBlank()) { "projectId was empty" }
        require(objectName.isNotBlank()) { "objectName was empty" }

        return downloadObjectPort.getDownloadSignedUrl("$projectId/$objectName", signedUrlProperties.duration).toMono()
    }
}
