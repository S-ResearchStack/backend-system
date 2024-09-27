package researchstack.backend.application.port.outgoing.study

interface DeleteStudyOutPort {
    suspend fun deleteStudy(studyId: String)
}
