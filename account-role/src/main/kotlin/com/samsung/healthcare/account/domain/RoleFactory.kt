package com.samsung.healthcare.account.domain

import com.samsung.healthcare.account.domain.Role.ProjectRole
import com.samsung.healthcare.account.domain.Role.ProjectRole.CustomRole
import com.samsung.healthcare.account.domain.Role.ProjectRole.HeadResearcher
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.account.domain.Role.ServiceAccount
import com.samsung.healthcare.account.domain.Role.TeamAdmin

object RoleFactory {

    fun createRole(role: String): Role =
        when (role) {
            Role.TEAM_ADMIN -> TeamAdmin
            Role.SERVICE_ACCOUNT -> ServiceAccount
            else -> createProjectRole(role)
        }

    private fun createProjectRole(role: String): Role {
        require(role.count { ch -> ch == ProjectRole.SEPARATOR } == 1)
        val separatorIndex = role.indexOf(ProjectRole.SEPARATOR)
        return createProjectRole(
            projectId = role.substring(0, separatorIndex),
            roleName = role.substring(separatorIndex + 1)
        )
    }

    private fun createProjectRole(projectId: String, roleName: String): Role {
        return when (roleName) {
            Role.HEAD_RESEARCHER -> HeadResearcher(projectId)
            Role.RESEARCHER -> Researcher(projectId)
            Role.PROJECT_OWNER -> ProjectOwner(projectId)
            else -> CustomRole(projectId, roleName)
        }
    }
}
