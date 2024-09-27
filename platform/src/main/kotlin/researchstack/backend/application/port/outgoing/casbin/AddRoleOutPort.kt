package researchstack.backend.application.port.outgoing.casbin

interface AddRoleOutPort {
    suspend fun addRole(userId: String, studyId: String, role: String): Boolean

    suspend fun addParticipantRole(userId: String, studyId: String): Boolean

    suspend fun addRolesForMyself(userId: String)
}
