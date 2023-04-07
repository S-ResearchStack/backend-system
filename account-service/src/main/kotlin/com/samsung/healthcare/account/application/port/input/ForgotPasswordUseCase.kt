package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Email
import reactor.core.publisher.Mono

interface ForgotPasswordUseCase {
    fun forgotPassword(email: Email): Mono<Void>
}
