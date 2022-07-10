package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Role
import reactor.core.publisher.Mono

interface RegisterRolesUseCase {
    fun registerRoles(roles: Collection<Role>): Mono<Void>

    fun createProjectRoles(createProjectRoleRequest: CreateProjectRoleRequest): Mono<Void>
}
