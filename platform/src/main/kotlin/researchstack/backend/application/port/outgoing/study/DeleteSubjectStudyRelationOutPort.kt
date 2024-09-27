package researchstack.backend.application.port.outgoing.study

interface DeleteSubjectStudyRelationOutPort {
    suspend fun deleteSubjectStudyRelation(subjectId: String, studyId: String)
}
