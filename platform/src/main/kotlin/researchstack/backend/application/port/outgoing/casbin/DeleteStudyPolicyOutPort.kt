package researchstack.backend.application.port.outgoing.casbin

interface DeleteStudyPolicyOutPort {
    suspend fun deleteStudyPolicies(studyId: String)
}
