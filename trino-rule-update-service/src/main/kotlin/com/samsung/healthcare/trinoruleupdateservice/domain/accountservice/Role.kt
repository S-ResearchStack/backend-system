package com.samsung.healthcare.trinoruleupdateservice.domain.accountservice

data class Role(
    val position: String,
    val projectId: String?,
) {
    companion object {
        private const val SEPARATOR = ':'
        fun newRole(role: String): Role {

            return when (role) {
                "team-admin" -> Role(role, null)
                "service-account" -> Role(role, null)
                else -> {
                    require(role.count { it == SEPARATOR } == 1)
                    role.split(SEPARATOR).let {
                        val projectId = it[0]
                        val position = it[1]
                        require(projectId.isNotBlank() and position.isNotBlank())
                        Role(position = it[1], projectId = it[0])
                    }
                }
            }
        }
    }
}
