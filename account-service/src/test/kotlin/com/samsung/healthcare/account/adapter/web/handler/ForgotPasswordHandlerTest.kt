package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.router.FORGOT_PASSWORD_PATH
import com.samsung.healthcare.account.adapter.web.router.ForgotPasswordRouter
import com.samsung.healthcare.account.application.exception.UnknownEmailException
import com.samsung.healthcare.account.application.service.ForgotPasswordService
import com.samsung.healthcare.account.domain.Email
import io.mockk.every
import org.assertj.core.api.Assertions
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
    ForgotPasswordHandler::class,
    ForgotPasswordRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    SecurityConfig::class,
)
internal class ForgotPasswordHandlerTest {
    @MockkBean
    private lateinit var forgotPasswordService: ForgotPasswordService

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    @Tag(POSITIVE_TEST)
    fun `forgotPassword should return ok`() {
        val email = "cubist@reserch-hub.test.com"
        every { forgotPasswordService.forgotPassword(Email(email)) } returns Mono.empty()

        val result = webClient.post(FORGOT_PASSWORD_PATH, ForgotPasswordHandler.ForgotPasswordRequest(email))
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `forgotPassword should return bad request when email is not valid`() {
        val result = webClient.post(
            FORGOT_PASSWORD_PATH,
            ForgotPasswordHandler.ForgotPasswordRequest("invalid-email")
        )
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `forgotPassword should return not found when email was not registered`() {
        val email = "cubist@reserch-hub.test.com"
        every { forgotPasswordService.forgotPassword(Email(email)) } throws UnknownEmailException()

        val result = webClient.post(
            FORGOT_PASSWORD_PATH,
            ForgotPasswordHandler.ForgotPasswordRequest(email)
        )
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.NOT_FOUND)
    }
}
