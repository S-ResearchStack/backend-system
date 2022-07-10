package com.samsung.healthcare.account.domain

import org.springframework.security.core.GrantedAuthority

interface GlobalAuthority : GrantedAuthority

object CreateStudyAuthority : GlobalAuthority {
    override fun getAuthority(): String = "create-study"
}

object CreateRoleAuthority : GlobalAuthority {
    override fun getAuthority(): String = "create-role"
}

interface ProjectAuthority : GrantedAuthority

data class AssignRoleAuthority(val projectId: String) : ProjectAuthority {

    override fun getAuthority(): String = "assign-role:$projectId"
}

data class AccessProjectAuthority(val projectId: String) : ProjectAuthority {

    override fun getAuthority(): String = "access-project:$projectId"
}
