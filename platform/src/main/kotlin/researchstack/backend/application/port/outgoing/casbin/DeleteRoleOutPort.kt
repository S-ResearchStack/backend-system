package researchstack.backend.application.port.outgoing.casbin

interface DeleteRoleOutPort {
    suspend fun deleteRolesFromStudy(userId: String, studyId: String)

    suspend fun deleteParticipantRolesFromStudy(userId: String, studyId: String)

    suspend fun deleteRolesForMyself(userId: String)
}
