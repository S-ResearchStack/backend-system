package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.accesscontrol.Requires
import com.samsung.healthcare.account.application.port.input.ListUserUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Role.ProjectRole.HeadResearcher
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ListUserService(
    private val authService: AuthServicePort
) : ListUserUseCase {
    override fun listAllUsers(): Mono<List<Account>> =
        authService.listUsers()

    @Requires([AccessProjectAuthority::class])
    override fun usersOfProject(projectId: String): Mono<List<Account>> {
        val projectRoles = listOf(
            ProjectOwner(projectId),
            HeadResearcher(projectId),
            Researcher(projectId)
        )
        return authService.retrieveUsersAssociatedWithRoles(projectRoles)
    }
}
