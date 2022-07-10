package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.CreateProjectRoleRequest
import com.samsung.healthcare.account.application.port.input.RegisterRolesUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class CreateRoleHandler(
    private val registerRolesService: RegisterRolesUseCase
) {
    fun createProjectRoles(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<CreateProjectRoleRequest>()
            .log()
            .flatMap { createRequest ->
                registerRolesService.createProjectRoles(createRequest)
            }
            .then(ServerResponse.ok().build())
}
