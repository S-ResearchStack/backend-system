package researchstack.backend.application.port.incoming.study

interface DeleteStudyUseCase {
    suspend fun deleteStudy(studyId: String)
}
