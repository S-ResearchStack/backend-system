package researchstack.backend.application.port.outgoing.casbin

interface UpdateRoleOutPort {
    suspend fun updateRole(userId: String, studyId: String, role: String): String
}
