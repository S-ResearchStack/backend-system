package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.router.SIGN_UP_PATH
import com.samsung.healthcare.account.adapter.web.router.SignUpRouter
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.application.port.input.SignUpCommand
import com.samsung.healthcare.account.application.port.input.SignUpUseCase
import com.samsung.healthcare.account.domain.Email
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
    SignUpHandler::class,
    SignUpRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    SecurityConfig::class,
)
internal class SignUpHandlerTest {
    @MockkBean
    private lateinit var signUpService: SignUpUseCase

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    private val email = Email("cubist@reserch-hub.test.com")
    private val password = "secret"
    private val profile: Map<String, Any> = emptyMap()

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return ok`() {
        every { signUpService.signUp(SignUpCommand(email, password, mapOf())) } returns Mono.empty()

        val result = webClient.post(SIGN_UP_PATH, TestRequest(email.value, password, profile))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when email is not valid`() {
        val result = webClient.post(SIGN_UP_PATH, TestRequest("invalid-email", password, profile))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when email is null`() {
        val result = webClient.post(SIGN_UP_PATH, TestRequest(null, password, profile))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when password is null`() {
        val result = webClient.post(SIGN_UP_PATH, TestRequest(email.value, null, profile))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request when password is not valid`() {
        val result = webClient.post(SIGN_UP_PATH, TestRequest(email.value, "", profile))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    private data class TestRequest(val email: String?, val password: String?, val profile: Map<String, Any>?)
}
