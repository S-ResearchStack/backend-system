package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import reactor.core.publisher.Mono

interface AccountServicePort {
    fun inviteUser(email: Email, roles: Collection<Role>): Mono<Void>

    fun assignRoles(accountId: String, roles: Collection<Role>): Mono<Void>

    fun removeRolesFromAccount(accountId: String, roles: Collection<Role>): Mono<Void>
}
