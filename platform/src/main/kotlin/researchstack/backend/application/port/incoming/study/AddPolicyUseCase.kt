package researchstack.backend.application.port.incoming.study

interface AddPolicyUseCase {
    suspend fun addPolicy(ptype: String, vararg vargs: String)
}
