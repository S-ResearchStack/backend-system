package com.samsung.healthcare.account.application.port.input

import reactor.core.publisher.Mono

interface UpdateAccountProfileUseCase {
    fun updateProfile(updateAccountProfileCommand: UpdateAccountProfileCommand): Mono<Map<String, Any>>
}
