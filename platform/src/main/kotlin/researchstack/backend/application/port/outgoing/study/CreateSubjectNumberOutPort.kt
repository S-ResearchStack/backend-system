package researchstack.backend.application.port.outgoing.study

interface CreateSubjectNumberOutPort {
    suspend fun createSubjectNumber(studyId: String, subjectId: String): String
}
