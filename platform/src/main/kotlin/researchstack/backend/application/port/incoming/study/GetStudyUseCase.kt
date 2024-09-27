package researchstack.backend.application.port.incoming.study

interface GetStudyUseCase {
    suspend fun getStudy(studyId: String): StudyResponse
    suspend fun getStudyByParticipationCode(participationCode: String): StudyResponse
    suspend fun getStudyList(): List<StudyResponse>
    suspend fun getStudyListByUser(investigatorId: String): List<StudyResponse>
    suspend fun getPublicStudyList(): List<StudyResponse>
    suspend fun getParticipatedStudyList(subjectId: String): List<StudyResponse>
    suspend fun getCreateStudyCommand(): CreateStudyCommandEnum
}
