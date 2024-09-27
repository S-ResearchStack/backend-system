package researchstack.backend.application.port.incoming.study

interface WithdrawFromStudyUseCase {
    suspend fun withdrawFromStudy(subjectId: String, studyId: String)
}
