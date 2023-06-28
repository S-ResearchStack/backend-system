package com.samsung.healthcare.platform.adapter.web.project

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.web.exception.ErrorResponse
import com.samsung.healthcare.platform.adapter.web.exception.ExceptionHandler
import com.samsung.healthcare.platform.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.TenantHandlerFilterFunction
import com.samsung.healthcare.platform.adapter.web.security.SecurityConfig
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.GlobalErrorAttributes
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.application.port.input.project.CreateInLabVisitCommand
import com.samsung.healthcare.platform.application.port.input.project.CreateInLabVisitUseCase
import com.samsung.healthcare.platform.application.port.input.project.GetInLabVisitUseCase
import com.samsung.healthcare.platform.application.port.input.project.UpdateInLabVisitCommand
import com.samsung.healthcare.platform.application.port.input.project.UpdateInLabVisitUseCase
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.project.InLabVisit
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.flow.flowOf
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime

@WebFluxTest
@Import(
    InLabVisitHandler::class,
    InLabVisitRouter::class,
    JwtAuthenticationFilterFunction::class,
    TenantHandlerFilterFunction::class,
    SecurityConfig::class,
    ExceptionHandler::class,
    GlobalErrorAttributes::class
)
internal class InLabVisitHandlerTest {
    @MockkBean
    private lateinit var createInLabVisitUseCase: CreateInLabVisitUseCase

    @MockkBean
    private lateinit var updateInLabVisitUseCase: UpdateInLabVisitUseCase

    @MockkBean
    private lateinit var getInLabVisitUseCase: GetInLabVisitUseCase

    @MockkBean
    private lateinit var getAccountUseCase: GetAccountUseCase

    @MockkBean
    private lateinit var getProjectQuery: GetProjectQuery

    @Autowired
    private lateinit var webTestClient: WebTestClient

    private val projectId = Project.ProjectId.from(1)
    private val jwt = ""
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.ResearchAssistant(projectId.value.toString()))
    )

    @BeforeEach
    fun setup() {
        coEvery {
            getProjectQuery.existsProject(any())
        } returns true
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createInLabVisit should throw unauthorized when no token`() {
        val result = webTestClient.post()
            .uri("/api/projects/${projectId.value}/in-lab-visits")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        Assertions.assertThat(result.responseBody?.message).isEqualTo("unauthorized")
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createInLabVisit should return BadRequest when it failed to read body`() {
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)

        val result = webTestClient.post()
            .uri("/api/projects/${projectId.value}/in-lab-visits")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .body(BodyInserters.fromValue(emptyMap<String, String>()))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createInLabVisit should work properly`() {
        val command = CreateInLabVisitCommand("u1", "c1", LocalDateTime.now(), LocalDateTime.now(), "")
        val inLabVisitId = 1

        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        coEvery { createInLabVisitUseCase.createInLabVisit(projectId.value.toString(), command) } returns InLabVisit(
            inLabVisitId, command.userId, command.checkedInBy, command.startTime, command.endTime, command.notes, ""
        )

        val result = webTestClient.post()
            .uri("/api/projects/${projectId.value}/in-lab-visits")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .body(BodyInserters.fromValue(command))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(result.responseHeaders.location)
            .isEqualTo(URI("/api/projects/${projectId.value}/in-lab-visits/$inLabVisitId"))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateInLabVisit should return BadRequest when it failed to read body`() {
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)

        val result = webTestClient.patch()
            .uri("/api/projects/${projectId.value}/in-lab-visits/1")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .body(BodyInserters.fromValue(emptyMap<String, String>()))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "1a", " ", "0x14", "1.1"])
    @Tag(NEGATIVE_TEST)
    fun `updateInLabVisit should return BadRequest when it received illegal inLabVisitId`(inLabVisitId: String) {
        val cmd = UpdateInLabVisitCommand("u1", "c1", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null)

        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        coEvery { updateInLabVisitUseCase.updateInLabVisit(any(), any(), any()) } returns
            InLabVisit(1, cmd.userId, cmd.checkedInBy, cmd.startTime, cmd.endTime, cmd.notes, "")

        val result = webTestClient.patch()
            .uri("/api/projects/${projectId.value}/in-lab-visits/$inLabVisitId")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .body(BodyInserters.fromValue(cmd))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInLabVisit should work properly`() {
        val inLabVisitId = 1
        val cmd = UpdateInLabVisitCommand("u1", "c1", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null)

        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        coEvery {
            updateInLabVisitUseCase.updateInLabVisit(projectId.value.toString(), inLabVisitId, cmd)
        } returns InLabVisit(inLabVisitId, "u1", "c1", cmd.startTime, cmd.endTime, "", "")

        val result = webTestClient.patch()
            .uri("/api/projects/${projectId.value}/in-lab-visits/$inLabVisitId")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .body(BodyInserters.fromValue(cmd))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "1a", " ", "0x14", "1.1"])
    @Tag(NEGATIVE_TEST)
    fun `getInLabVisit should return BadRequest when it received illegal inLabVisitId`(inLabVisitId: String) {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)

        val result = webTestClient.get()
            .uri("/api/projects/${projectId.value}/in-lab-visits/$inLabVisitId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInLabVisit should work properly`() {
        val inLabVisitId = 1

        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        coEvery { getInLabVisitUseCase.getInLabVisitById(projectId.value.toString(), inLabVisitId) } returns InLabVisit(
            inLabVisitId, "u1", "c1", LocalDateTime.now(), LocalDateTime.now(), "", ""
        )

        val result = webTestClient.get()
            .uri("/api/projects/${projectId.value}/in-lab-visits/$inLabVisitId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `listInLabVisits should work properly`() {
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        coEvery { getInLabVisitUseCase.getInLabVisits(projectId.value.toString()) } returns flowOf<InLabVisit>(
            InLabVisit(
                1, "u1", "c1", LocalDateTime.now(), LocalDateTime.now(), "", ""
            ),
            InLabVisit(
                1, "u1", "c1", LocalDateTime.now(), LocalDateTime.now(), "", ""
            )
        )

        val result = webTestClient.get()
            .uri("/api/projects/${projectId.value}/in-lab-visits")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
    }
}
