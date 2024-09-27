package researchstack.backend.application.port.outgoing.study

interface DeleteSubjectNumberOutPort {
    suspend fun deleteSubjectNumber(studyId: String, subjectId: String)
}
