package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.account.adapter.web.handler.InvitationHandler.InvitationRequest
import com.samsung.healthcare.account.adapter.web.handler.InvitationHandler.InvitationResult
import com.samsung.healthcare.account.adapter.web.router.INVITATION_PATH
import com.samsung.healthcare.account.adapter.web.router.InvitationRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.service.AccountService
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.ProjectRole.ResearchAssistant
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

@WebFluxTest
@Import(
    InvitationHandler::class,
    InvitationRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtAuthenticationFilterFunction::class,
    SecurityConfig::class,
)
internal class InvitationHandlerTest {

    @MockkBean
    private lateinit var accountService: AccountService

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    private val email = Email("cubist@reserch-hub.test.com")
    private val invitedRole = ResearchAssistant("project-id")

    private val normalRequest =
        InvitationRequest(email = email.value, roles = listOf(invitedRole.roleName))

    private val jwt =
        "eyJhb...6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkw...MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    val testAccount = Account("id", Email("cubist@research-hub.test.com"), emptyList())

    @BeforeEach
    fun beforeEach() {
        every { getAccountService.getAccountFromToken(jwt) } returns Mono.just(testAccount)
        webClient = webClient.mutate()
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .build()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when body is not a valid format`() {
        val result = webClient.post(INVITATION_PATH, "invalid")
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when email is not valid`() {
        val result = webClient.post(
            INVITATION_PATH,
            listOf(
                normalRequest.copy(email = "invalid-email")
            )
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when role name is not valid`() {
        val result = webClient.post(
            INVITATION_PATH,
            listOf(
                normalRequest.copy(roles = listOf("invalid-role"))
            )
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return ok`() {
        every { accountService.inviteUser(email, any()) } returns Mono.empty()

        val result = webClient.post(
            INVITATION_PATH,
            listOf(normalRequest)
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return multi-status if some people could not be invited`() {
        val failedEmail = Email("failed@research-hub.test.com")
        every { accountService.inviteUser(email, any()) } returns Mono.empty()
        every { accountService.inviteUser(failedEmail, any()) } returns Mono.error(RuntimeException())

        val result = webClient.post(
            INVITATION_PATH,
            listOf(
                normalRequest,
                normalRequest.copy(email = failedEmail.value)
            )
        )
            .expectBodyList(InvitationResult::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.MULTI_STATUS)
        assertThat(result.responseBody?.size).isEqualTo(1)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when role key dose not exist`() {
        val result = webClient.post(
            INVITATION_PATH,
            listOf(
                mapOf(
                    "email" to email.value,
                    "" to invitedRole.roleName
                )
            )
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
