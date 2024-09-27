package researchstack.backend.application.port.outgoing.study

interface DeleteParticipationRequirementOutPort {
    suspend fun delete(studyId: String)
}
