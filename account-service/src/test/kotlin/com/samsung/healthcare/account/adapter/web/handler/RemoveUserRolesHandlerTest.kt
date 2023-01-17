package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
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
    @Tag(NEGATIVE_TEST)
    fun `removeUserRoles should return bad request when account-id is null`() {
        val result = webClient.post(REMOVE_USER_ROLE_PATH, normalRequest.copy(accountId = null))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeUserRoles should return bad request when roles is null`() {
        val result = webClient.post(REMOVE_USER_ROLE_PATH, normalRequest.copy(roles = null))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeUserRoles should return bad request when account-id is empty`() {
        val result = webClient.post(REMOVE_USER_ROLE_PATH, normalRequest.copy(accountId = ""))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeUserRoles should return bad request when roles is empty`() {
        val result = webClient.post(REMOVE_USER_ROLE_PATH, normalRequest.copy(roles = listOf()))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeUserRoles should return bad request when roles has invalid role-name`() {
        val result = webClient.post(REMOVE_USER_ROLE_PATH, normalRequest.copy(roles = listOf("invalid-role")))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `removeUserRoles should return ok`() {
        every {
            accountService.removeRolesFromAccount(
                normalRequest.accountId!!,
                normalRequest.roles!!.map { RoleFactory.createRole(it) }
            )
        } returns Mono.empty()

        val result = webClient.post(REMOVE_USER_ROLE_PATH, normalRequest)
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    data class TestRequest(val accountId: String?, val roles: List<String>? = emptyList())
}
