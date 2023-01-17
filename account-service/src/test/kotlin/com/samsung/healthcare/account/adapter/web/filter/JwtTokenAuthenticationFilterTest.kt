package com.samsung.healthcare.account.adapter.web.filter

import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.context.ContextHolder.ACCOUNT_KEY
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class JwtTokenAuthenticationFilterTest {

    private val getAccountService = mockk<GetAccountUseCase>()

    private val jwtTokenAuthenticationFilter = JwtTokenAuthenticationFilter(getAccountService)

    private val filterChain = WebFilterChain { Mono.empty() }
    val account = Account("id", Email("cubist@research-hub.test.com"), emptyList())

    @Test
    @Tag(POSITIVE_TEST)
    fun `filter should set account in context`() {
        val jwt =
            "eyJhb...6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkw...MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

        val exchange = MockServerWebExchange.from(
            MockServerHttpRequest
                .get("test")
                .header(AUTHORIZATION, "Bearer $jwt")
        )

        every { getAccountService.getAccountFromToken(jwt) } returns Mono.just(account)

        StepVerifier.create(
            jwtTokenAuthenticationFilter.filter(exchange, filterChain)
        ).expectAccessibleContext()
            .contains(ACCOUNT_KEY, account)
            .then()
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `filter should throw JwtException`() {
        val invalidToken = "invalid-token"

        val exchange = MockServerWebExchange.from(
            MockServerHttpRequest
                .get("test")
                .header(AUTHORIZATION, "Bearer $invalidToken")
        )

        every { getAccountService.getAccountFromToken(invalidToken) } returns Mono.error(JwtException("invalid-token"))

        StepVerifier.create(
            jwtTokenAuthenticationFilter.filter(exchange, filterChain)
        ).verifyError(JwtException::class.java)
    }
}
