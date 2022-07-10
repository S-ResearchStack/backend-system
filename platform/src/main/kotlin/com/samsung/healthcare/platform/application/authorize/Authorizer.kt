package com.samsung.healthcare.platform.application.authorize

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Role.ProjectRole
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.domain.Project.ProjectId
import org.springframework.security.core.GrantedAuthority
import reactor.core.publisher.Mono

object Authorizer {
    fun getAccount(authority: GrantedAuthority): Mono<Account> =
        ContextHolder.getAccount()
            .filter {
                it.hasPermission(authority)
            }
            .switchIfEmpty(
                Mono.error(ForbiddenException("You don't have permission. (${authority.authority})"))
            )

    fun getAccessibleProjects(): Mono<List<ProjectId>> =
        ContextHolder.getAccount()
            .map {
                it.roles
                    .filterIsInstance<ProjectRole>()
                    .map { projectRole -> ProjectId.from(projectRole.projectId.toInt()) }
            }
}
