package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.ResetPasswordCommand
import com.samsung.healthcare.account.application.port.input.ResetPasswordUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ResetPasswordService(
    private val authServicePort: AuthServicePort
) : ResetPasswordUseCase {
    override fun resetPassword(passwordResetCommand: ResetPasswordCommand): Mono<String> =
        authServicePort.resetPassword(passwordResetCommand.resetToken, passwordResetCommand.password)
}
