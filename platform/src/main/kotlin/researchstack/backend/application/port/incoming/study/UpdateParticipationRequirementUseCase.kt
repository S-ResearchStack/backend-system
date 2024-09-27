package researchstack.backend.application.port.incoming.study

interface UpdateParticipationRequirementUseCase {
    suspend fun update(studyId: String, command: UpdateParticipationRequirementCommand)
}
