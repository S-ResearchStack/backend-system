package researchstack.backend.application.port.outgoing.investigator

interface DeleteInvestigatorStudyRelationOutPort {
    suspend fun deleteRelationByEmail(email: String, studyId: String)

    suspend fun deleteByStudyId(studyId: String)
}
