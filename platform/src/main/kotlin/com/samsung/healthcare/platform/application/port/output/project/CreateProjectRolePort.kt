package com.samsung.healthcare.platform.application.port.output.project

import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.platform.domain.Project.ProjectId
import reactor.core.publisher.Mono

interface CreateProjectRolePort {
    fun createProjectRoles(account: Account, projectId: ProjectId): Mono<Void>
}
