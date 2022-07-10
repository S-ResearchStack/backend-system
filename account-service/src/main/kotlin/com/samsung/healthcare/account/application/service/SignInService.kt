package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.SignInCommand
import com.samsung.healthcare.account.application.port.input.SignInResponse
import com.samsung.healthcare.account.application.port.input.SignInUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.application.port.output.JwtGenerationCommand
import com.samsung.healthcare.account.application.port.output.TokenSigningPort
import com.samsung.healthcare.account.domain.Account
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SignInService(
    private val authServicePort: AuthServicePort,
    private val tokenSigningPort: TokenSigningPort
) : SignInUseCase {
    override fun signIn(signInCommand: SignInCommand): Mono<SignInResponse> =
        authServicePort.signIn(signInCommand.email, signInCommand.password)
            .flatMap { account -> generateToken(account) }

    private fun generateToken(account: Account): Mono<SignInResponse> =
        tokenSigningPort.generateSignedJWT(
            JwtGenerationCommand(
                // TODO config issuer
                issuer = "research-hub.com",
                subject = account.id,
                email = account.email.value,
                roles = account.roles,
                // TODO config lifetime
                lifeTime = 1 * 60 * 60 * 24,
            )
        ).map { encodedJwt ->
            SignInResponse(account, encodedJwt)
        }
}
