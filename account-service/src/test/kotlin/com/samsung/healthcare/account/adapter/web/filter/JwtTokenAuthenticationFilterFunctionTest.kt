package com.samsung.healthcare.account.adapter.web.filter

import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.application.exception.UnauthorizedException
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

internal class JwtTokenAuthenticationFilterFunctionTest {

    private val getAccountService = mockk<GetAccountUseCase>()

    private val jwtTokenAuthenticationFilter = JwtAuthenticationFilterFunction(getAccountService)

    val account = Account("id", Email("cubist@research-hub.test.com"), emptyList())

    @Test
    @Tag(NEGATIVE_TEST)
    fun `filter should throw JwtException`() {
        val invalidToken = "invalid-token"

        val request = MockServerRequest.builder()
            .header(AUTHORIZATION, "Bearer $invalidToken")
            .build()

        every { getAccountService.getAccountFromToken(invalidToken) } returns Mono.error(JwtException("invalid-token"))

        StepVerifier.create(
            jwtTokenAuthenticationFilter.filter(request) {
                ServerResponse.ok().build()
            }
        ).verifyError<UnauthorizedException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `filter should throw IllegalAccessException when token is not valid`() {
        val request = MockServerRequest.builder()
            .header(AUTHORIZATION, "")
            .build()
        assertThrows<UnauthorizedException> {
            jwtTokenAuthenticationFilter.filter(request) {
                ServerResponse.ok().build()
            }
        }
    }
}
