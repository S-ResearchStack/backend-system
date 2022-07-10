package com.samsung.healthcare.account.application.port.input

import reactor.core.publisher.Mono

interface SignInUseCase {
    fun signIn(signInCommand: SignInCommand): Mono<SignInResponse>
}
