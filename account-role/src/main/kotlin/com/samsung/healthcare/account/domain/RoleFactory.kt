package com.samsung.healthcare.account.domain

import com.samsung.healthcare.account.domain.Role.ProjectRole
import com.samsung.healthcare.account.domain.Role.ProjectRole.DataScientist
import com.samsung.healthcare.account.domain.Role.ProjectRole.PrincipalInvestigator
import com.samsung.healthcare.account.domain.Role.ProjectRole.StudyCreator
import com.samsung.healthcare.account.domain.Role.ProjectRole.ResearchAssistant
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
        require(roleName.isNotBlank())
        return when (roleName) {
            Role.PRINCIPAL_INVESTIGATOR -> PrincipalInvestigator(projectId)
            Role.RESEARCH_ASSISTANT -> ResearchAssistant(projectId)
            Role.STUDY_CREATOR -> StudyCreator(projectId)
            Role.DATA_SCIENTIST -> DataScientist(projectId)
            else -> throw IllegalArgumentException()
        }
    }
}
