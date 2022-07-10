package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.adapter.web.handler.AssignRoleHandler.RoleRequest
import com.samsung.healthcare.account.application.service.AccountService
import com.samsung.healthcare.account.domain.RoleFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class RemoveUserRolesHandler(
    private val accountService: AccountService
) {

    fun removeUserRoles(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<RoleRequest>()
            .flatMap { roleRequest ->
                accountService.removeRolesFromAccount(
                    roleRequest.accountId,
                    roleRequest.roles.map { RoleFactory.createRole(it) }
                )
            }.flatMap { ServerResponse.ok().build() }
}
