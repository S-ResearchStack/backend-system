package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Account
import reactor.core.publisher.Mono

interface ResetPasswordUseCase {
    fun resetPassword(passwordResetCommand: ResetPasswordCommand): Mono<Account>
}
