package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtTokenAuthenticationFilter
import com.samsung.healthcare.account.adapter.web.router.REMOVE_USER_ROLE_PATH
import com.samsung.healthcare.account.adapter.web.router.RemoveUserRolesRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.service.AccountService
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.account.domain.RoleFactory
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@Import(
    RemoveUserRolesHandler::class,
    RemoveUserRolesRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtTokenAuthenticationFilter::class,
    SecurityConfig::class,
)
internal class RemoveUserRolesHandlerTest {
    @MockkBean
    private lateinit var accountService: AccountService

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    private val normalRequest =
        TestRequest("account-id", roles = listOf(Researcher("project-1").roleName))

    @Test
    fun `removeUserRoles should return bad request when account-id is null`() {
        webClient.post(REMOVE_USER_ROLE_PATH, normalRequest.copy(accountId = null))
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `removeUserRoles should return bad request when roles is null`() {
        webClient.post(REMOVE_USER_ROLE_PATH, normalRequest.copy(roles = null))
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `removeUserRoles should return ok`() {
        every {
            accountService.removeRolesFromAccount(
                normalRequest.accountId!!,
                normalRequest.roles!!.map { RoleFactory.createRole(it) }
            )
        } returns Mono.empty()

        webClient.post(REMOVE_USER_ROLE_PATH, normalRequest)
            .expectStatus()
            .isOk
    }

    data class TestRequest(val accountId: String?, val roles: List<String>? = emptyList())
}
