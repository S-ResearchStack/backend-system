package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.CreateStudyAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.port.input.CreateProjectCommand
import com.samsung.healthcare.platform.application.port.input.CreateProjectUseCase
import com.samsung.healthcare.platform.application.port.output.CreateProjectPort
import com.samsung.healthcare.platform.application.port.output.project.CreateProjectRolePort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CreateProjectService(
    private val createProjectPort: CreateProjectPort,
    private val createProjectRolePort: CreateProjectRolePort
) : CreateProjectUseCase {

    /**
     * Registers a new project with the given specifications. First checks that the user is authorized, then utilizes [createProject].
     *
     * @param command The [CreateProjectCommand] with the user-provided specifications.
     * @throws [com.samsung.healthcare.platform.application.exception.ForbiddenException] when the requesting [Account] does not have project creation authority.
     * @return The [ProjectId] of the newly registered project
     */
    override suspend fun registerProject(command: CreateProjectCommand): ProjectId =
        Authorizer.getAccount(CreateStudyAuthority)
            .flatMap { account ->
                createProject(account, command)
            }.awaitSingle()

    /**
     * Creates a new project associated with the given user account.
     *
     * @param account The [Account] to be registered in the new project.
     * @param command The [CreateProjectCommand] with the user-provided specifications.
     * @return A [Mono] instance holding the generated [ProjectId].
     */
    private fun createProject(account: Account, command: CreateProjectCommand): Mono<ProjectId> =
        mono {
            createProjectPort.create(
                Project.newProject(command.name, command.info, command.isOpen)
            )
        }.flatMap { projectId ->
            createProjectRolePort.createProjectRoles(account, projectId)
                .then(Mono.just(projectId))
        }.doOnError { it.printStackTrace() }
}
