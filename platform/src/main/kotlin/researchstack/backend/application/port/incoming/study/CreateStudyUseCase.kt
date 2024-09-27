package researchstack.backend.application.port.incoming.study

interface CreateStudyUseCase {
    suspend fun createStudy(investigatorId: String, createStudyCommand: CreateStudyCommand): CreateStudyResponse
}
