package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.CreateProjectRoleRequest
import com.samsung.healthcare.account.application.port.input.RegisterRolesUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.DataScientist
import com.samsung.healthcare.account.domain.Role.ProjectRole.PrincipalInvestigator
import com.samsung.healthcare.account.domain.Role.ProjectRole.StudyCreator
import com.samsung.healthcare.account.domain.Role.ProjectRole.ResearchAssistant
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
            listOf(StudyCreator(createProjectRoleRequest.projectId))
        )

    private fun projectRoles(projectId: String): Collection<Role> = listOf(
        StudyCreator(projectId),
        PrincipalInvestigator(projectId),
        ResearchAssistant(projectId),
        DataScientist(projectId),
    )
}
