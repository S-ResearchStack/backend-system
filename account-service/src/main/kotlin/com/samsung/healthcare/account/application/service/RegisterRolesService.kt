package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.CreateProjectRoleRequest
import com.samsung.healthcare.account.application.port.input.RegisterRolesUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.HeadResearcher
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RegisterRolesService(
    private val authServicePort: AuthServicePort
) : RegisterRolesUseCase {
    override fun registerRoles(roles: Collection<Role>): Mono<Void> =
        if (roles.isEmpty()) Mono.error(IllegalArgumentException())
        else authServicePort.createRoles(roles)

    override fun createProjectRoles(createProjectRoleRequest: CreateProjectRoleRequest): Mono<Void> =
        authServicePort.createRoles(projectRoles(createProjectRoleRequest.projectId))
            .then(
                setOwnerRoleToAccount(createProjectRoleRequest)
            )

    private fun setOwnerRoleToAccount(createProjectRoleRequest: CreateProjectRoleRequest) =
        authServicePort.assignRoles(
            createProjectRoleRequest.accountId,
            listOf(ProjectOwner(createProjectRoleRequest.projectId))
        )

    private fun projectRoles(projectId: String): Collection<Role> = listOf(
        ProjectOwner(projectId),
        HeadResearcher(projectId),
        Researcher(projectId)
    )
}
