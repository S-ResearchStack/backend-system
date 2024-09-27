package researchstack.backend.application.port.incoming.education

interface UpdateEducationalContentUseCase {
    suspend fun updateEducationalContent(
        studyId: String,
        contentId: String,
        command: UpdateEducationalContentCommand
    )
}
