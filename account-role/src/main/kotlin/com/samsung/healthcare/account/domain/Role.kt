package com.samsung.healthcare.account.domain

import org.springframework.security.core.GrantedAuthority

sealed class Role private constructor(val roleName: String) {
    companion object {
        const val TEAM_ADMIN = "team-admin"
        const val SERVICE_ACCOUNT = "service-account"
        const val STUDY_CREATOR = "study-creator"
        const val PRINCIPAL_INVESTIGATOR = "principal-investigator"
        const val RESEARCH_ASSISTANT = "research-assistant"
        const val DATA_SCIENTIST = "data-scientist"
    }

    abstract val authorities: Collection<GrantedAuthority>

    object TeamAdmin : Role(TEAM_ADMIN) {
        override val authorities: Collection<GrantedAuthority> = listOf(
            CreateStudyAuthority
        )
    }

    // NOTE how to handle access control for service.
    object ServiceAccount : Role(SERVICE_ACCOUNT) {
        override val authorities: Collection<GrantedAuthority> = listOf(
            CreateRoleAuthority
        )
    }

    sealed class ProjectRole private constructor(val projectId: String, val projectRoleName: String) :
        Role("$projectId$SEPARATOR$projectRoleName") {

        companion object {
            const val SEPARATOR = ':'
        }

        init {
            require(projectId.isNotBlank())
        }

        fun canAccessProject(pid: String): Boolean = projectId == pid

        class StudyCreator(projectId: String) : ProjectRole(projectId, STUDY_CREATOR) {
            override val authorities: Collection<GrantedAuthority> = listOf(
                AssignRoleAuthority(projectId),
                ReadStudyOverviewAuthority(projectId),
                ReadParticipantDataAuthority(projectId),
                ReadDeIdentifiedParticipantDataAuthority(projectId),
                AccessInLabVisitAuthority(projectId),
                AccessTaskAuthority(projectId),
                AccessDocumentAuthority(projectId),
                ReadAggSensorDataAuthority(projectId),
                QueryRawDataAuthority(projectId),
                QueryDeIdentifiedDataAuthority(projectId),
                ReadProjectMemberAuthority(projectId),
                AccessProjectMemberAuthority(projectId),
            )
        }

        class PrincipalInvestigator(projectId: String) : ProjectRole(projectId, PRINCIPAL_INVESTIGATOR) {
            override val authorities: Collection<GrantedAuthority> = listOf(
                AssignRoleAuthority(projectId),
                ReadStudyOverviewAuthority(projectId),
                ReadParticipantDataAuthority(projectId),
                ReadDeIdentifiedParticipantDataAuthority(projectId),
                AccessInLabVisitAuthority(projectId),
                AccessTaskAuthority(projectId),
                AccessDocumentAuthority(projectId),
                ReadAggSensorDataAuthority(projectId),
                QueryRawDataAuthority(projectId),
                QueryDeIdentifiedDataAuthority(projectId),
                ReadProjectMemberAuthority(projectId),
                AccessProjectMemberAuthority(projectId),
            )
        }

        class ResearchAssistant(projectId: String) : ProjectRole(projectId, RESEARCH_ASSISTANT) {
            override val authorities: Collection<GrantedAuthority> = listOf(
                ReadStudyOverviewAuthority(projectId),
                ReadParticipantDataAuthority(projectId),
                ReadDeIdentifiedParticipantDataAuthority(projectId),
                AccessInLabVisitAuthority(projectId),
                AccessTaskAuthority(projectId),
                AccessDocumentAuthority(projectId),
                ReadAggSensorDataAuthority(projectId),
                QueryRawDataAuthority(projectId),
                QueryDeIdentifiedDataAuthority(projectId),
                ReadProjectMemberAuthority(projectId),
                AccessProjectMemberAuthority(projectId),
            )
        }

        class DataScientist(projectId: String) : ProjectRole(projectId, DATA_SCIENTIST) {
            override val authorities: Collection<GrantedAuthority> = listOf(
                ReadStudyOverviewAuthority(projectId),
                ReadDeIdentifiedParticipantDataAuthority(projectId),
                AccessTaskAuthority(projectId),
                AccessDocumentAuthority(projectId),
                ReadAggSensorDataAuthority(projectId),
                QueryDeIdentifiedDataAuthority(projectId),
                ReadProjectMemberAuthority(projectId),
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Role) return false

        if (roleName != other.roleName) return false

        return true
    }

    override fun hashCode(): Int {
        return roleName.hashCode()
    }

    fun hasAuthority(authority: GrantedAuthority): Boolean =
        authorities.contains(authority)
}
