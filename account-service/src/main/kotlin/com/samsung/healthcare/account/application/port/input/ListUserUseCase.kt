package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Account
import reactor.core.publisher.Mono

interface ListUserUseCase {
    fun listAllUsers(): Mono<List<Account>>
    fun usersOfProject(projectId: String): Mono<List<Account>>
}
