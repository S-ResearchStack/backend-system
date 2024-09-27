package researchstack.backend.application.port.incoming.study

interface DeleteParticipationRequirementUseCase {
    suspend fun delete(studyId: String)
}
