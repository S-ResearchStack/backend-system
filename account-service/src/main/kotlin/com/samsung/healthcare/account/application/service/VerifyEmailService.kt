package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.VerifyEmailResponse
import com.samsung.healthcare.account.application.port.input.VerifyEmailUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Email
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class VerifyEmailService(
    private val authServicePort: AuthServicePort,
    private val tokenService: TokenService,
    private val mailService: MailService,
) : VerifyEmailUseCase {
    override fun verifyEmail(token: String): Mono<VerifyEmailResponse> =
        authServicePort.verifyEmail(token)
            .flatMap { account ->
                tokenService.generateToken(account)
                    .map { VerifyEmailResponse(account, it.accessToken, it.refreshToken) }
            }

    override fun resendVerificationEmail(email: Email): Mono<Void> =
        authServicePort.generateEmailVerificationToken(email)
            .flatMap { token ->
                mailService.sendVerificationMail(email, token)
            }
}
