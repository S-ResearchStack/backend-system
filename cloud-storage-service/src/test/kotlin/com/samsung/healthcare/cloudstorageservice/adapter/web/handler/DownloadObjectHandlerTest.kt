package com.samsung.healthcare.cloudstorageservice.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.cloudstorageservice.NEGATIVE_TEST
import com.samsung.healthcare.cloudstorageservice.POSITIVE_TEST
import com.samsung.healthcare.cloudstorageservice.adapter.web.config.SecurityConfig
import com.samsung.healthcare.cloudstorageservice.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.cloudstorageservice.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.DOWNLOAD_OBJECT_PATH
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.DOWNLOAD_OBJECT_URL_PATH
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.DownloadObjectRouter
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.PARTICIPANT_DOWNLOAD_OBJECT_PATH
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.ParticipantDownloadObjectRouter
import com.samsung.healthcare.cloudstorageservice.application.port.input.DownloadObjectUseCase
import com.samsung.healthcare.cloudstorageservice.application.port.output.ExistsUserPort
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.URL

@WebFluxTest
@Import(
    DownloadObjectHandler::class,
    DownloadObjectRouter::class,
    ParticipantDownloadObjectRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    IdTokenFilterFunction::class,
    JwtAuthenticationFilterFunction::class,
    SecurityConfig::class,
)
internal class DownloadObjectHandlerTest {
    @MockkBean
    private lateinit var existsUserPort: ExistsUserPort

    @MockkBean
    private lateinit var downloadObjectUseCase: DownloadObjectUseCase

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    private val jwt =
        "eyJhb...6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkw...MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        emptyList(),
    )

    @BeforeEach
    fun beforeEach() {
        every { getAccountService.getAccountFromToken(jwt) } returns Mono.just(account)
        webClient = webClient.mutate()
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .build()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `download should work properly`() {
        val objectName = "test-name"
        every { downloadObjectUseCase.getSignedUrl("1", objectName) } returns
            URL("https://test").toMono()

        val result = webClient.get()
            .uri("$DOWNLOAD_OBJECT_PATH?object_name=$objectName".replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.TEMPORARY_REDIRECT)
        assertThat(result.responseHeaders).containsEntry(
            HttpHeaders.LOCATION,
            listOf("https://test")
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `download throws BadRequestException when the object_name was not set`() {
        val result = webClient.get()
            .uri(DOWNLOAD_OBJECT_PATH.replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDownloadUrl should work properly`() {
        val objectName = "test-name"
        val urlDuration = 120L
        every { downloadObjectUseCase.getSignedUrl("1", objectName, urlDuration) } returns
            URL("https://test").toMono()

        val result = webClient.get()
            .uri(
                "$DOWNLOAD_OBJECT_URL_PATH?object_name=$objectName&url_duration=$urlDuration"
                    .replace("{projectId}", "1")
            )
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
        assertThat(result.responseBody).isEqualTo("https://test".toByteArray())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getDownloadUrl throws BadRequestException when the object_name was not set`() {
        val result = webClient.get()
            .uri(DOWNLOAD_OBJECT_URL_PATH.replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getDownloadUrl throws IllegalArgumentException when the url_duration was wrong format`() {
        val result = webClient.get()
            .uri("$DOWNLOAD_OBJECT_URL_PATH?object_name=test-name&url_duration=wrong".replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `participantDownload should work properly`() {
        val objectName = "test-name"
        every { downloadObjectUseCase.getParticipantSignedUrl("1", objectName) } returns
            URL("https://test").toMono()
        every { existsUserPort.exists(any(), any()) } returns Mono.empty()

        val result = webClient.get()
            .uri("$PARTICIPANT_DOWNLOAD_OBJECT_PATH?object_name=$objectName".replace("{projectId}", "1"))
            .header("id-token", "testToken")
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.TEMPORARY_REDIRECT)
        assertThat(result.responseHeaders).containsEntry(
            HttpHeaders.LOCATION,
            listOf("https://test")
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `participantDownload throws UnauthorizedException if id token is not provided`() {
        val objectName = "test-name"

        val result = webClient.get()
            .uri("$PARTICIPANT_DOWNLOAD_OBJECT_PATH?object_name=$objectName".replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `participantDownload throws BadRequestException when the object_name was not set`() {
        every { existsUserPort.exists(any(), any()) } returns Mono.empty()

        val result = webClient.get()
            .uri(PARTICIPANT_DOWNLOAD_OBJECT_PATH.replace("{projectId}", "1"))
            .header("id-token", "testToken")
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
