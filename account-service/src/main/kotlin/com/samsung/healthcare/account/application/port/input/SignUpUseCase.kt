package com.samsung.healthcare.account.application.port.input

import reactor.core.publisher.Mono

interface SignUpUseCase {
    fun signUp(signUpCommand: SignUpCommand): Mono<Void>
}
