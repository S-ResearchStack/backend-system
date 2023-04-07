package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.ForgotPasswordUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Email
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ForgotPasswordService(
    private val authServicePort: AuthServicePort,
    private val mailService: MailService,
) : ForgotPasswordUseCase {
    override fun forgotPassword(email: Email): Mono<Void> =
        authServicePort.generateResetToken(email)
            .flatMap { resetToken ->
                mailService.sendPasswordResetMail(email, resetToken)
            }
}
