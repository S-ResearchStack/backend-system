package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.handler.VerifyEmailHandler.ResendVerificationEmailRequest
import com.samsung.healthcare.account.adapter.web.handler.VerifyEmailHandler.VerifyEmailRequest
import com.samsung.healthcare.account.adapter.web.router.RESEND_VERIFICATION_EMAIL_PATH
import com.samsung.healthcare.account.adapter.web.router.VERIFY_EMAIL_PATH
import com.samsung.healthcare.account.adapter.web.router.VerifyEmailRouter
import com.samsung.healthcare.account.application.exception.InvalidEmailVerificationTokenException
import com.samsung.healthcare.account.application.port.input.SignInResponse
import com.samsung.healthcare.account.application.service.VerifyEmailService
import com.samsung.healthcare.account.domain.Account
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
    VerifyEmailHandler::class,
    VerifyEmailRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    SecurityConfig::class,
)
internal class VerifyEmailHandlerTest {
    @MockkBean
    private lateinit var verifyEmailService: VerifyEmailService

    @Autowired
    private lateinit var webClient: WebTestClient

    private val token = "test-token"

    @Test
    @Tag(POSITIVE_TEST)
    fun `verifyEmail should return ok`() {
        every { verifyEmailService.verifyEmail(token) } returns Mono.just(
            SignInResponse(
                Account("id", Email("cubist@reserch-hub.test.com"), emptyList()),
                "accessToken",
                "refreshToken"
            )
        )

        val result = webClient.post(VERIFY_EMAIL_PATH, VerifyEmailRequest(token))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `resendVerificationEmail should return ok`() {
        val email = "cubist@reserch-hub.test.com"
        every { verifyEmailService.resendVerificationEmail(Email(email)) } returns Mono.empty()

        val result = webClient.post(RESEND_VERIFICATION_EMAIL_PATH, ResendVerificationEmailRequest(email))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `resendVerificationEmail should return bad request when email is not valid`() {
        val result = webClient.post(RESEND_VERIFICATION_EMAIL_PATH, ResendVerificationEmailRequest("invalid-email"))
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `verifyEmail should return unauthorized when verify token is not valid`() {

        every { verifyEmailService.verifyEmail(token) } throws InvalidEmailVerificationTokenException()

        val result = webClient.post(VERIFY_EMAIL_PATH, VerifyEmailRequest(token))
            .expectBody()
            .returnResult()
        // why UNAUTHORIZED?
        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
