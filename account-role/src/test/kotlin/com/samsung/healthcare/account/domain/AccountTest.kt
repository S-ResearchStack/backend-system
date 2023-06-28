package com.samsung.healthcare.account.domain

import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.domain.Role.ProjectRole.DataScientist
import com.samsung.healthcare.account.domain.Role.ProjectRole.PrincipalInvestigator
import com.samsung.healthcare.account.domain.Role.ProjectRole.ResearchAssistant
import com.samsung.healthcare.account.domain.Role.ProjectRole.StudyCreator
import com.samsung.healthcare.account.domain.Role.ServiceAccount
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.UUID

internal class AccountTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `Account with service account role is able to create role`() {
        assertTrue(
            accountWith(ServiceAccount).canCreateRole()
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Account without service account role is not able to create role`() {
        assertFalse(
            accountWith(TeamAdmin).canCreateRole()
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Account with study creator on project-id is able to assign role for that project`() {
        val projectId = "project-x"
        assertTrue(
            accountWith(StudyCreator(projectId))
                .canAssignProjectRole(projectId)
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Account with study creator on project-id is not able to assign role for other project`() {
        val projectId = "project-x"
        assertFalse(
            accountWith(StudyCreator(projectId))
                .canAssignProjectRole("project-y")
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Account that is not study creator on project-id is not able to assign role for that project`() {
        val projectId = "project-x"
        assertFalse(accountWith(PrincipalInvestigator(projectId)).canAssignProjectRole(projectId))
        assertFalse(accountWith(ResearchAssistant(projectId)).canAssignProjectRole(projectId))
        assertFalse(accountWith(DataScientist(projectId)).canAssignProjectRole(projectId))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Account with role can access project and without role cannot access to project`() {
        val projectXId = "project-x"
        val projectYId = "project-y"
        assertTrue(accountWith(StudyCreator(projectXId)).canAccessProject(projectXId))
        assertFalse(accountWith(StudyCreator(projectXId)).canAccessProject(projectYId))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Account with research assistant and data scientist should be restricted for some authorities`() {
        val projectXId = "project-x"
        val researchAssistant = accountWith(ResearchAssistant(projectXId))
        val dataScientist = accountWith(DataScientist(projectXId))

        assertFalse(
            researchAssistant.roles.any {
                role ->
                role.authorities.any { it.authority == AssignRoleAuthority(projectXId).authority }
            }
        )

        assertFalse(
            dataScientist.roles.any {
                role ->
                role.authorities.any {
                    it.authority == ReadParticipantDataAuthority(projectXId).authority ||
                        it.authority == AccessInLabVisitAuthority(projectXId).authority ||
                        it.authority == QueryRawDataAuthority(projectXId).authority ||
                        it.authority == AccessProjectMemberAuthority(projectXId).authority
                }
            }
        )
    }

    private fun accountWith(vararg roles: Role): Account =
        Account(
            UUID.randomUUID().toString(),
            Email("someone@gmail.com"),
            roles.asList()
        )
}
