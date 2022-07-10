package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.exception.SignInException
import com.samsung.healthcare.account.application.port.input.SignInCommand
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.application.port.output.TokenSigningPort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

internal class SignInServiceTest {
    private val authServicePort = mockk<AuthServicePort>()

    private val tokenSigningPort = mockk<TokenSigningPort>()

    private val signInService = SignInService(authServicePort, tokenSigningPort)

    private val email = Email("cubist@reserach-hub.test.com")
    private val password = "pw"

    @Test
    fun `signIn should return account with jwt`() {

        val account = Account("id", email, listOf(TeamAdmin))
        every { authServicePort.signIn(email, password) } returns Mono.just(account)
        every {
            tokenSigningPort.generateSignedJWT(
                match { jwtGenerationCommand ->
                    jwtGenerationCommand.email == email.value &&
                        jwtGenerationCommand.subject == account.id &&
                        jwtGenerationCommand.roles == account.roles
                }
            )
        } returns Mono.just("encoded-jwt-string")

        StepVerifier.create(
            signInService.signIn(SignInCommand(email, password))
        ).expectNextMatches { signInResponse ->
            signInResponse.account == account &&
                signInResponse.jwt.isNotBlank()
        }.verifyComplete()
    }

    @Test
    fun `signIn should throw SignInException when signIn failed`() {
        every { authServicePort.signIn(email, "pw") } returns Mono.error(SignInException())
        StepVerifier.create(
            signInService.signIn(SignInCommand(email, "pw"))
        ).verifyError<SignInException>()
    }
}
