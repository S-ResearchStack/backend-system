package researchstack.backend.application.port.incoming.education

interface DeleteEducationalContentUseCase {
    suspend fun deleteEducationalContent(studyId: String, contentId: String)
}
