package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.accesscontrol.Requires
import com.samsung.healthcare.account.application.port.input.ListUserUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.ReadProjectMemberAuthority
import com.samsung.healthcare.account.domain.Role.ProjectRole.DataScientist
import com.samsung.healthcare.account.domain.Role.ProjectRole.PrincipalInvestigator
import com.samsung.healthcare.account.domain.Role.ProjectRole.StudyCreator
import com.samsung.healthcare.account.domain.Role.ProjectRole.ResearchAssistant
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ListUserService(
    private val authService: AuthServicePort
) : ListUserUseCase {
    override fun listAllUsers(): Mono<List<Account>> =
        authService.listUsers()

    @Requires([ReadProjectMemberAuthority::class])
    override fun usersOfProject(projectId: String): Mono<List<Account>> {
        val projectRoles = listOf(
            StudyCreator(projectId),
            PrincipalInvestigator(projectId),
            ResearchAssistant(projectId),
            DataScientist(projectId),
        )
        return authService.retrieveUsersAssociatedWithRoles(projectRoles)
    }
}
