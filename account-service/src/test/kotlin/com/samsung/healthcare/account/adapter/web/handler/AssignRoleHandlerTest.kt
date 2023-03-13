package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.JacksonConfig
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.account.adapter.web.router.ASSIGN_ROLE_PATH
import com.samsung.healthcare.account.adapter.web.router.AssignRoleRouter
import com.samsung.healthcare.account.application.exception.UnknownAccountIdException
import com.samsung.healthcare.account.application.exception.UnknownRoleException
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.service.AccountService
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
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
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@Import(
    AssignRoleHandler::class,
    AssignRoleRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtAuthenticationFilterFunction::class,
    SecurityConfig::class,
    JacksonConfig::class
)
internal class AssignRoleHandlerTest {
    @MockkBean
    private lateinit var accountService: AccountService

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
    fun `should return ok`() {
        every { accountService.assignRoles(any(), any()) } returns Mono.empty()
        webClient.mutate().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        val result = webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest("test-account", listOf(Researcher("project-id").roleName)),
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when account id is null`() {
        val result = webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest(null, listOf(Researcher("project-id").roleName)),
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when roles is null`() {
        val result = webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest("account-id", null),
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return not-found when roles is null`() {
        every { accountService.assignRoles(any(), any()) } returns Mono.error(UnknownRoleException())

        val result = webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest("account-id", listOf("1:researcher")),
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return not found when account-id does not exist`() {
        every { accountService.assignRoles(any(), any()) } returns Mono.error(UnknownAccountIdException())

        val result = webClient.put(
            ASSIGN_ROLE_PATH,
            TestRequest("account-id", listOf("1:researcher")),
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when body is empty`() {

        val result = webClient.put().uri(ASSIGN_ROLE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    data class TestRequest(val accountId: String?, val roles: List<String>? = emptyList())
}
