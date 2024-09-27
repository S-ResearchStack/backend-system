package researchstack.backend.application.port.incoming.study

interface UpdateStudyUseCase {
    suspend fun updateStudy(studyId: String, updateStudyCommand: UpdateStudyCommand)
}
