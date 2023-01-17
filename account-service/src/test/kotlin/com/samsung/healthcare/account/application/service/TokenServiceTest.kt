package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.exception.ExpiredRefreshTokenException
import com.samsung.healthcare.account.application.exception.InvalidTokenException
import com.samsung.healthcare.account.application.port.input.TokenRefreshCommand
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.application.port.output.TokenSigningPort
import com.samsung.healthcare.account.application.port.output.TokenStoragePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import com.samsung.healthcare.account.domain.Token
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Instant
import java.util.UUID

class TokenServiceTest {

    private val authServicePort = mockk<AuthServicePort>()
    private val tokenSigningPort = mockk<TokenSigningPort>()
    private val tokenStoragePort = mockk<TokenStoragePort>()

    private val tokenService = TokenService(tokenSigningPort, tokenStoragePort, authServicePort)

    private val accountId = UUID.randomUUID().toString()
    private val tokenRefreshCommand = TokenRefreshCommand("access-token", "refreshtokensample")
    private val token = Token(
        accountId,
        tokenRefreshCommand.jwt,
        tokenRefreshCommand.refreshToken,
        Instant.now().plusSeconds(1000L)
    )
    private val email = Email("cubist@reserach-hub.test.com")
    private val account = Account(accountId, email, listOf(TeamAdmin))

    @Test
    @Tag(POSITIVE_TEST)
    fun `refreshToken should return new tokens when accessToken and refreshToken is valid`() {
        every {
            tokenStoragePort.findByAccessTokenAndRefreshToken(tokenRefreshCommand.jwt, tokenRefreshCommand.refreshToken)
        } returns Mono.just(token)

        every { authServicePort.getAccount(accountId) } returns Mono.just(account)
        every { tokenSigningPort.generateSignedJWT(any()) } returns Mono.just(token.accessToken)
        every { tokenStoragePort.save(any()) } returns Mono.empty()
        every { tokenStoragePort.deleteByRefreshToken(tokenRefreshCommand.refreshToken) } returns Mono.empty()

        StepVerifier.create(
            tokenService.refreshToken(tokenRefreshCommand)
        ).expectNextMatches { refreshTokenResponse ->
            refreshTokenResponse.refreshToken.isNotBlank() &&
                refreshTokenResponse.jwt.isNotBlank()
        }.verifyComplete()

        verify { tokenStoragePort.deleteByRefreshToken(tokenRefreshCommand.refreshToken) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `refreshToken should throw ExpiredRefreshTokenException when refreshToken is expired`() {
        every {
            tokenStoragePort.findByAccessTokenAndRefreshToken(tokenRefreshCommand.jwt, tokenRefreshCommand.refreshToken)
        } returns Mono.just(token.copy(expiredAt = Instant.now().minusSeconds(1000L)))

        StepVerifier.create(
            tokenService.refreshToken(tokenRefreshCommand)
        ).verifyError(ExpiredRefreshTokenException::class.java)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `refreshToken should throw InvalidTokenException when refreshToken is expired`() {
        every {
            tokenStoragePort.findByAccessTokenAndRefreshToken(tokenRefreshCommand.jwt, tokenRefreshCommand.refreshToken)
        } returns Mono.empty()

        StepVerifier.create(
            tokenService.refreshToken(tokenRefreshCommand)
        ).verifyError(InvalidTokenException::class.java)
    }
}
