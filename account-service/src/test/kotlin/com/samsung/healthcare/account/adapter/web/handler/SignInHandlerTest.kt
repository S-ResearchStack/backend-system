package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtTokenAuthenticationFilter
import com.samsung.healthcare.account.adapter.web.router.SIGN_IN_PATH
import com.samsung.healthcare.account.adapter.web.router.SignInRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.port.input.SignInCommand
import com.samsung.healthcare.account.application.port.input.SignInUseCase
import com.samsung.healthcare.account.domain.Email
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@Import(
    SignInHandler::class,
    SignInRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtTokenAuthenticationFilter::class,
    SecurityConfig::class,
)
internal class SignInHandlerTest {
    @MockkBean
    private lateinit var signInService: SignInUseCase

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    private val email = Email("cubist@reserch-hub.test.com")

    @Test
    fun `should return ok`() {
        val pw = "secret"
        every { signInService.signIn(SignInCommand(email, pw)) } returns Mono.empty()

        webClient.post(SIGN_IN_PATH, TestRequest(email.value, pw))
            .expectStatus()
            .isOk
    }

    @Test
    fun `should return bad request when email is not valid`() {
        webClient.post(SIGN_IN_PATH, TestRequest("invalid-email", "secret"))
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `should return bad request when email is null`() {
        webClient.post(SIGN_IN_PATH, TestRequest(null, "secret"))
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `should return bad request when password is null`() {
        webClient.post(SIGN_IN_PATH, TestRequest(email.value, null))
            .expectStatus()
            .isBadRequest
    }

    @Test
    fun `should return bad request when password is not valid`() {
        webClient.post(SIGN_IN_PATH, TestRequest(email.value, ""))
            .expectStatus()
            .isBadRequest
    }

    private data class TestRequest(val email: String?, val password: String?)
}
