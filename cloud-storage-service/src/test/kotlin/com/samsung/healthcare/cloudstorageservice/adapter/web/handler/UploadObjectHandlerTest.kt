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
import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.ProjectIdFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.PARTICIPANT_UPLOAD_OBJECT_URL_PATH
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.ParticipantUploadObjectRouter
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.UPLOAD_OBJECT_URL_PATH
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.UploadObjectRouter
import com.samsung.healthcare.cloudstorageservice.application.port.input.UploadObjectUseCase
import com.samsung.healthcare.cloudstorageservice.application.port.output.ExistsUserPort
import io.mockk.every
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.URL

@WebFluxTest
@Import(
    UploadObjectHandler::class,
    UploadObjectRouter::class,
    ParticipantUploadObjectRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    IdTokenFilterFunction::class,
    JwtAuthenticationFilterFunction::class,
    ProjectIdFilterFunction::class,
    SecurityConfig::class,
)
internal class UploadObjectHandlerTest {
    @MockkBean
    private lateinit var existsUserPort: ExistsUserPort

    @MockkBean
    private lateinit var uploadObjectUseCase: UploadObjectUseCase

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
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
            .build()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `upload should work properly`() {
        val objectName = "test-name"
        every { uploadObjectUseCase.getSignedUrl("1", objectName) } returns
            URL("https://test").toMono()

        val result = webClient.get()
            .uri("$UPLOAD_OBJECT_URL_PATH?&object_name=$objectName".replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `upload throws BadRequestException when the object_name field was not set`() {
        val objectName = "test-name"
        every { uploadObjectUseCase.getSignedUrl("1", objectName) } returns
            URL("https://test").toMono()

        val result = webClient.get()
            .uri(UPLOAD_OBJECT_URL_PATH.replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(String(result.responseBody!!)).contains("no object_name")
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `participantUpload should work properly`() {
        val objectName = "test-name"
        every { uploadObjectUseCase.getParticipantSignedUrl("1", objectName) } returns
            URL("https://test").toMono()
        every { existsUserPort.exists(any(), any()) } returns Mono.empty()

        val result = webClient.get()
            .uri("$PARTICIPANT_UPLOAD_OBJECT_URL_PATH?object_name=$objectName".replace("{projectId}", "1"))
            .header("id-token", "testToken")
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `participantUpload throws UnauthorizedException if id token is not provided`() {
        val objectName = "test-name"

        val result = webClient.get()
            .uri("$PARTICIPANT_UPLOAD_OBJECT_URL_PATH?object_name=$objectName".replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `participantUpload throws BadRequestException when the object_name was not set`() {
        every { existsUserPort.exists(any(), any()) } returns Mono.empty()

        val result = webClient.get()
            .uri(PARTICIPANT_UPLOAD_OBJECT_URL_PATH.replace("{projectId}", "1"))
            .header("id-token", "testToken")
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
