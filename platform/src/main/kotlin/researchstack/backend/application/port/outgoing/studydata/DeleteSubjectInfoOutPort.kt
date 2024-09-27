package researchstack.backend.application.port.outgoing.studydata

interface DeleteSubjectInfoOutPort {
    suspend fun deleteSubjectInfo(
        studyId: String,
        subjectId: String
    )
}
