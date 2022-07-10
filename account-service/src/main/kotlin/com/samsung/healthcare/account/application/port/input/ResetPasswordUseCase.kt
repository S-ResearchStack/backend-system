package com.samsung.healthcare.account.application.port.input

import reactor.core.publisher.Mono

interface ResetPasswordUseCase {
    fun resetPassword(passwordResetCommand: ResetPasswordCommand): Mono<String>
}
