package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.exception.UnverifiedEmailException
import com.samsung.healthcare.account.application.port.input.SignInCommand
import com.samsung.healthcare.account.application.port.input.SignInResponse
import com.samsung.healthcare.account.application.port.input.SignInUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class SignInService(
    private val authServicePort: AuthServicePort,
    private val tokenService: TokenService,
) : SignInUseCase {
    override fun signIn(signInCommand: SignInCommand): Mono<SignInResponse> =
        authServicePort.signIn(signInCommand.email, signInCommand.password)
            .flatMap { account ->
                authServicePort.isVerifiedEmail(account.id, account.email)
                    .filter { it }
                    .switchIfEmpty { Mono.error(UnverifiedEmailException()) }
                    .flatMap {
                        tokenService.generateToken(account)
                    }
                    .map { SignInResponse(account, it.accessToken, it.refreshToken) }
            }
}
