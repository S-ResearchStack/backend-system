package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.account.adapter.web.router.CREATE_ROLE_PATH
import com.samsung.healthcare.account.adapter.web.router.CreateRoleRouter
import com.samsung.healthcare.account.application.port.input.CreateProjectRoleRequest
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.service.RegisterRolesService
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
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
    CreateRoleHandler::class,
    CreateRoleRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtAuthenticationFilterFunction::class,
    SecurityConfig::class,
)
internal class CreateRoleHandlerTest {
    @MockkBean
    private lateinit var registerRolesService: RegisterRolesService

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
        every { registerRolesService.createProjectRoles(any()) } returns Mono.empty()

        val result = webClient.put(
            CREATE_ROLE_PATH,
            CreateProjectRoleRequest("account-id", "project-id")
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when account-id is not given`() {
        val result = webClient.put(
            CREATE_ROLE_PATH,
            mapOf("projectId" to "project-id")
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when project-id is not given`() {
        val result = webClient.put(
            CREATE_ROLE_PATH,
            mapOf("accountId" to "accountid")
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when account-id is not empty`() {
        val result = webClient.put(
            CREATE_ROLE_PATH,
            mapOf(
                "accountId" to "",
                "projectId" to "project-id"
            )
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when project-id is not empty`() {
        val result = webClient.put(
            CREATE_ROLE_PATH,
            mapOf(
                "accountId" to "test-account-id",
                "projectId" to ""
            )
        )
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
