package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Email
import reactor.core.publisher.Mono

interface VerifyEmailUseCase {
    fun verifyEmail(token: String): Mono<VerifyEmailResponse>

    fun resendVerificationEmail(email: Email): Mono<Void>
}
