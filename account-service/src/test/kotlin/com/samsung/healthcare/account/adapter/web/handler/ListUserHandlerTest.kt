package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.account.adapter.web.router.LIST_ALL_USER_PATH
import com.samsung.healthcare.account.adapter.web.router.LIST_PROJECT_USER_PATH
import com.samsung.healthcare.account.adapter.web.router.ListUserRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.service.ListUserService
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.RoleFactory
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
    ListUserHandler::class,
    ListUserRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtAuthenticationFilterFunction::class,
    SecurityConfig::class,
)
internal class ListUserHandlerTest {
    @MockkBean
    private lateinit var listUserService: ListUserService

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

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
    @Tag(POSITIVE_TEST)
    fun `list project users should return ok`() {
        val role = RoleFactory.createRole(Role.TEAM_ADMIN)
        val account = Account("account-id", Email("test@research-hub.test.com"), listOf(role))
        every { listUserService.usersOfProject(any()) } returns Mono.just(listOf(account))

        val result = webClient.get("$LIST_PROJECT_USER_PATH?projectId=1")
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `list all users should return ok`() {
        val role = RoleFactory.createRole(Role.TEAM_ADMIN)
        val account = Account("account-id", Email("test@research-hub.test.com"), listOf(role))
        every { listUserService.listAllUsers() } returns Mono.just(listOf(account))

        val result = webClient.get(LIST_ALL_USER_PATH)
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }
}
