package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.ListUserUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.queryParamOrNull
import reactor.core.publisher.Mono

@Component
class ListUserHandler(
    private val listUserService: ListUserUseCase
) {
    fun listProjectUsers(req: ServerRequest): Mono<ServerResponse> {
        val projectId = req.queryParamOrNull("projectId") ?: throw IllegalArgumentException("projectId was empty")
        return listUsers(projectId)
            .flatMap { ServerResponse.ok().bodyValue(it) }
    }

    fun listAllUsers(req: ServerRequest): Mono<ServerResponse> =
        listUsers(null)
            .flatMap { ServerResponse.ok().bodyValue(it) }

    private fun listUsers(projectId: String?): Mono<List<UserResponse>> {
        val accounts = projectId?.let { id -> listUserService.usersOfProject(id) } ?: listUserService.listAllUsers()

        return accounts
            .map { users ->
                users.map { account ->
                    UserResponse(
                        account.id,
                        account.email.value,
                        account.roles.map { it.roleName },
                        account.profiles
                    )
                }
            }
    }

    data class UserResponse(val id: String, val email: String, val roles: List<String>, val profile: Map<String, Any>)
}
