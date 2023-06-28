package com.samsung.healthcare.cloudstorageservice.application.service

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.cloudstorageservice.NEGATIVE_TEST
import com.samsung.healthcare.cloudstorageservice.POSITIVE_TEST
import com.samsung.healthcare.cloudstorageservice.application.config.SignedUrlProperties
import com.samsung.healthcare.cloudstorageservice.application.port.output.DownloadObjectPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.net.URL

internal class DownloadObjectServiceTest {
    private val signedUrlProperties = SignedUrlProperties(120L)
    private val downloadObjectPort = mockk<DownloadObjectPort>()
    private val downloadObjectService = DownloadObjectService(signedUrlProperties, downloadObjectPort)

    private val projectId = "1"
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.ResearchAssistant(projectId)),
    )

    private val signedUrlDuration = 120L

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSignedUrl should work properly`() {
        val objectName = "test-name"
        mockkObject(ContextHolder)
        every { ContextHolder.getAccount() } returns Mono.just(account)
        every {
            downloadObjectPort.getDownloadSignedUrl("$projectId/$objectName", signedUrlDuration)
        } returns URL("https://test")

        StepVerifier.create(
            downloadObjectService.getSignedUrl(projectId, objectName, signedUrlDuration)
        )
            .expectNextMatches {
                it == URL("https://test")
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getSignedUrl should throw IllegalArgumentException when projectId was empty`(projectId: String) {
        assertThrows<IllegalArgumentException> {
            downloadObjectService.getSignedUrl(projectId, "test-name", signedUrlDuration)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getSignedUrl should throw IllegalArgumentException when objectName was empty`(objectName: String) {
        assertThrows<IllegalArgumentException> {
            downloadObjectService.getSignedUrl(projectId, objectName, signedUrlDuration)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipantSignedUrl should work properly`() {
        val objectName = "test-name"
        every {
            downloadObjectPort.getDownloadSignedUrl("$projectId/$objectName", signedUrlDuration)
        } returns URL("https://test")

        StepVerifier.create(
            downloadObjectService.getParticipantSignedUrl(projectId, objectName)
        )
            .expectNextMatches {
                it == URL("https://test")
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getParticipantSignedUrl should throw IllegalArgumentException when projectId was empty`(projectId: String) {
        assertThrows<IllegalArgumentException> {
            downloadObjectService.getParticipantSignedUrl(projectId, "test-name")
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getParticipantSignedUrl should throw IllegalArgumentException when objectName was empty`(objectName: String) {
        assertThrows<IllegalArgumentException> {
            downloadObjectService.getParticipantSignedUrl(projectId, objectName)
        }
    }
}
