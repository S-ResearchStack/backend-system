package com.samsung.healthcare.account.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.adapter.web.config.SecurityConfig
import com.samsung.healthcare.account.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.account.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.account.adapter.web.filter.JwtTokenAuthenticationFilter
import com.samsung.healthcare.account.adapter.web.router.REFRESH_TOKEN_PATH
import com.samsung.healthcare.account.adapter.web.router.TokenRefreshRouter
import com.samsung.healthcare.account.application.exception.ExpiredRefreshTokenException
import com.samsung.healthcare.account.application.exception.InvalidTokenException
import com.samsung.healthcare.account.application.port.input.TokenRefreshResponse
import com.samsung.healthcare.account.application.port.input.TokenRefreshUsecase
import io.mockk.every
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest(
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JwtTokenAuthenticationFilter::class]
        )
    ]
)
@Import(
    TokenRefreshHandler::class,
    TokenRefreshRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    SecurityConfig::class,
)
internal class TokenRefreshHandlerTest {
    @MockkBean
    private lateinit var tokenRefreshUsecase: TokenRefreshUsecase

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return ok with new token`() {
        val tokenRefreshResponse = TokenRefreshResponse("access.token.jwt", "RandomRefreshToken")
        every { tokenRefreshUsecase.refreshToken(any()) } returns Mono.just(tokenRefreshResponse)

        webClient.post(REFRESH_TOKEN_PATH, TestTokenRefreshCommand("jwt", "refreshToken"))
            .expectStatus()
            .isOk
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return NotFound when token is invalid`() {
        every { tokenRefreshUsecase.refreshToken(any()) } returns Mono.error(InvalidTokenException())

        webClient.post(REFRESH_TOKEN_PATH, TestTokenRefreshCommand("jwt", "refreshToken"))
            .expectStatus()
            .isNotFound
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return UnAuthorized when refreshToken is expired`() {
        every { tokenRefreshUsecase.refreshToken(any()) } returns Mono.error(ExpiredRefreshTokenException())

        webClient.post(REFRESH_TOKEN_PATH, TestTokenRefreshCommand("jwt", "refreshToken"))
            .expectStatus()
            .isUnauthorized
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when jwt is null`() {
        val tokenRefreshResponse = TokenRefreshResponse("access.token.jwt", "RandomRefreshToken")
        every { tokenRefreshUsecase.refreshToken(any()) } returns Mono.just(tokenRefreshResponse)

        webClient.post(REFRESH_TOKEN_PATH, TestTokenRefreshCommand(refreshToken = "refreshToken"))
            .expectStatus()
            .isBadRequest
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when jwt is blank`() {
        webClient.post(REFRESH_TOKEN_PATH, TestTokenRefreshCommand("    ", "refreshToken"))
            .expectStatus()
            .isBadRequest
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when refreshToken is null`() {
        webClient.post(REFRESH_TOKEN_PATH, TestTokenRefreshCommand(jwt = "jwt-token"))
            .expectStatus()
            .isBadRequest
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when refreshToken is blank`() {
        webClient.post(REFRESH_TOKEN_PATH, TestTokenRefreshCommand("access-token", "    "))
            .expectStatus()
            .isBadRequest
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when refreshToken is not given`() {
        webClient.post(REFRESH_TOKEN_PATH, mapOf("jwt" to "jwt-token"))
            .expectStatus()
            .isBadRequest
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return BadRequest when jwt is not given`() {
        webClient.post(REFRESH_TOKEN_PATH, mapOf("refreshToken" to "randomRefreshToken"))
            .expectStatus()
            .isBadRequest
    }

    private data class TestTokenRefreshCommand(val jwt: String? = null, val refreshToken: String? = null)
}
