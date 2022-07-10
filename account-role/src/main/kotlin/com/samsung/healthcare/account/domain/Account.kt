package com.samsung.healthcare.account.domain

import com.samsung.healthcare.account.domain.Role.ProjectRole
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ServiceAccount
import org.springframework.security.core.GrantedAuthority

data class Account(
    val id: String,
    val email: Email,
    val roles: Collection<Role>,
    val profiles: Map<String, Any> = emptyMap()
) {
    fun canAssignProjectRole(projectId: String): Boolean =
        roles.filterIsInstance<ProjectOwner>()
            .any { it.canAccessProject(projectId) }

    fun canCreateRole(): Boolean =
        roles.contains(ServiceAccount)

    fun hasPermission(authority: GrantedAuthority): Boolean =
        roles.any { it.hasAuthority(authority) }

    fun canAccessProject(projectId: String): Boolean =
        roles.filterIsInstance<ProjectRole>()
            .any { it.canAccessProject(projectId) }
}
