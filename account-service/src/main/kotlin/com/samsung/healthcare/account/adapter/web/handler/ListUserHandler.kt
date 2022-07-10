package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.ListUserUseCase
import com.samsung.healthcare.account.domain.Account
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.queryParamOrNull
import reactor.core.publisher.Mono

@Component
class ListUserHandler(
    private val listUserService: ListUserUseCase
) {
    fun listUsers(req: ServerRequest): Mono<ServerResponse> =
        listUsers(req.queryParamOrNull("projectId"))
            .flatMap { users ->
                ServerResponse.ok()
                    .bodyValue(
                        users.map { account ->
                            UserResponse(
                                account.id,
                                account.email.value,
                                account.roles.map { it.roleName },
                                account.profiles
                            )
                        }
                    )
            }

    private fun listUsers(projectId: String?): Mono<List<Account>> =
        projectId?.let { id -> listUserService.usersOfProject(id) }
            ?: listUserService.listAllUsers()

    data class UserResponse(val id: String, val email: String, val roles: List<String>, val profile: Map<String, Any>)
}
