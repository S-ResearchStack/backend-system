package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.service.AccountService
import com.samsung.healthcare.account.domain.RoleFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class AssignRoleHandler(
    private val accountService: AccountService
) {
    fun assignRoles(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<RoleRequest>()
            .flatMap { rolesRequest ->
                accountService.assignRoles(
                    rolesRequest.accountId,
                    rolesRequest.roles.map { RoleFactory.createRole(it) }
                )
            }
            .then(ServerResponse.ok().build())

    data class RoleRequest(val accountId: String, val roles: List<String> = emptyList())
}
