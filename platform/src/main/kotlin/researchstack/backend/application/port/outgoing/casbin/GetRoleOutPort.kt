package researchstack.backend.application.port.outgoing.casbin

interface GetRoleOutPort {
    suspend fun getRoles(userId: String): List<String>
    suspend fun getRolesInStudy(userId: String, studyId: String): List<String>
}
