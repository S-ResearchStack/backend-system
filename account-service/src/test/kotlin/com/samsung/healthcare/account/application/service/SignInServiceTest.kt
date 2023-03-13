package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.exception.SignInException
import com.samsung.healthcare.account.application.exception.UnverifiedEmailException
import com.samsung.healthcare.account.application.port.input.SignInCommand
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import com.samsung.healthcare.account.domain.Token
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

internal class SignInServiceTest {
    private val authServicePort = mockk<AuthServicePort>()

    private val tokenService = mockk<TokenService>()

    private val signInService = SignInService(authServicePort, tokenService)

    private val email = Email("cubist@reserach-hub.test.com")
    private val password = "pw"

    @Test
    @Tag(POSITIVE_TEST)
    fun `signIn should return properly`() {
        val account = Account("id", email, listOf(TeamAdmin))
        val encodedJwt = "encoded-jwt-string"
        every { authServicePort.signIn(email, password) } returns Mono.just(account)
        every { authServicePort.isVerifiedEmail(account.id, account.email) } returns Mono.just(true)
        every { tokenService.generateToken(account) } returns Mono.just(Token.generateToken(account.id, encodedJwt, 1))

        StepVerifier.create(
            signInService.signIn(SignInCommand(email, password))
        ).expectNextMatches { signInResponse ->
            signInResponse.account == account &&
                signInResponse.jwt == encodedJwt &&
                signInResponse.refreshToken.isNotBlank()
        }.verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `signIn should throw SignInException when signIn failed`() {
        every { authServicePort.signIn(email, "pw") } returns Mono.error(SignInException())
        StepVerifier.create(
            signInService.signIn(SignInCommand(email, "pw"))
        ).verifyError<SignInException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `signIn should throw UnverifiedEmailException when the email isn't verified yet`() {
        val account = Account("id", email, listOf(TeamAdmin))
        every { authServicePort.signIn(email, password) } returns Mono.just(account)
        every { authServicePort.isVerifiedEmail(account.id, account.email) } returns Mono.just(false)
        StepVerifier.create(
            signInService.signIn(SignInCommand(email, "pw"))
        ).verifyError<UnverifiedEmailException>()
    }
}
