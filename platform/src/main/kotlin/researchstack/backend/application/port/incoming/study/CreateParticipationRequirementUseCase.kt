package researchstack.backend.application.port.incoming.study

interface CreateParticipationRequirementUseCase {
    suspend fun create(command: CreateParticipationRequirementCommand, studyId: String)
}
