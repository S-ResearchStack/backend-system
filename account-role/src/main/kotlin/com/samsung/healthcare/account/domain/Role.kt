package com.samsung.healthcare.account.domain

import org.springframework.security.core.GrantedAuthority

sealed class Role private constructor(val roleName: String) {
    companion object {
        const val TEAM_ADMIN = "team-admin"
        const val SERVICE_ACCOUNT = "service-account"
        const val PROJECT_OWNER = "project-owner"
        const val HEAD_RESEARCHER = "head-researcher"
        const val RESEARCHER = "researcher"
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

        class ProjectOwner(projectId: String) : ProjectRole(projectId, PROJECT_OWNER) {
            override val authorities: Collection<GrantedAuthority> = listOf(
                AssignRoleAuthority(projectId),
                AccessProjectAuthority(projectId)
            )
        }

        class HeadResearcher(projectId: String) : ProjectRole(projectId, HEAD_RESEARCHER) {
            override val authorities: Collection<GrantedAuthority> = listOf(
                AssignRoleAuthority(projectId),
                AccessProjectAuthority(projectId)
            )
        }

        class Researcher(projectId: String) : ProjectRole(projectId, RESEARCHER) {
            override val authorities: Collection<GrantedAuthority> = listOf(
                AccessProjectAuthority(projectId)
            )
        }

        class CustomRole(projectId: String, projectRoleName: String) : ProjectRole(projectId, projectRoleName) {
            init {
                require(projectRoleName.isNotBlank())
            }

            override val authorities: Collection<GrantedAuthority> = listOf(
                AccessProjectAuthority(projectId)
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
