package researchstack.backend.application.port.incoming.education

interface CreateEducationalContentUseCase {
    suspend fun createEducationalContent(
        studyId: String,
        investigatorId: String,
        command: CreateEducationalContentCommand
    ): CreateEducationalContentResponse
}
