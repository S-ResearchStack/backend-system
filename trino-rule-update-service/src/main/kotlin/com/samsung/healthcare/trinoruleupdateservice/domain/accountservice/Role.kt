package com.samsung.healthcare.trinoruleupdateservice.domain.accountservice

data class Role(
    val position: String,
    val projectId: String?,
) {
    companion object {
        fun newRole(role: String): Role {
            return when (role) {
                "team-admin" -> Role(role, null)
                "service-account" -> Role(role, null)
                else -> role.split(':').let { Role(position = it[1], projectId = it[0]) }
            }
        }
    }
}
