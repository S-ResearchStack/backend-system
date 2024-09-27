package researchstack.backend.application.port.outgoing.casbin

interface CreateStudyPolicyOutPort {
    suspend fun createStudyPolicies(userId: String, studyId: String)
}
