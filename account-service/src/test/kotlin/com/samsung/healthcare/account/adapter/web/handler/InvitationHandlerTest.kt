package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtTokenAuthenticationFilter
import com.samsung.healthcare.account.adapter.web.handler.InvitationHandler.InvitationRequest
import com.samsung.healthcare.account.adapter.web.handler.InvitationHandler.InvitationResult
import com.samsung.healthcare.account.adapter.web.router.INVITATION_PATH
import com.samsung.healthcare.account.adapter.web.router.InvitationRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.service.AccountService
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus.MULTI_STATUS
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.core.publisher.Mono

@WebFluxTest
@Import(
    InvitationHandler::class,
    InvitationRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtTokenAuthenticationFilter::class,
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
    private val invitedRole = Researcher("project-id")

    private val normalRequest =
        InvitationRequest(email = email.value, roles = listOf(invitedRole.roleName))

    @Test
    fun `should return bad request when body is not a valid format`() {
        webClient.post(INVITATION_PATH, "invalid")
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `should return bad request when email is not valid`() {
        webClient.post(
            INVITATION_PATH,
            listOf(
                normalRequest.copy(email = "invalid-email")
            )
        ).expectStatus()
            .isBadRequest
    }

    @Test
    fun `should return bad request when role name is not valid`() {
        webClient.post(
            INVITATION_PATH,
            listOf(
                normalRequest.copy(roles = listOf("invalid-role"))
            )
        ).expectStatus()
            .isBadRequest
    }

    @Test
    fun `should return ok`() {
        every { accountService.inviteUser(email, any()) } returns Mono.empty()

        webClient.post(
            INVITATION_PATH,
            listOf(normalRequest)
        ).expectStatus()
            .isOk
    }

    @Test
    fun `should return multi-status if some people could not be invited`() {

        val failedEmail = Email("failed@research-hub.test.com")
        every { accountService.inviteUser(email, any()) } returns Mono.empty()
        every { accountService.inviteUser(failedEmail, any()) } returns Mono.error(RuntimeException())

        webClient.post(
            INVITATION_PATH,
            listOf(
                normalRequest,
                normalRequest.copy(email = failedEmail.value)
            )
        ).expectStatus()
            .isEqualTo(MULTI_STATUS)
            .expectBodyList<InvitationResult>()
            .hasSize(1)
    }
}
