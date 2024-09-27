package researchstack.backend.application.port.incoming.investigator

interface DeleteInvestigatorUseCase {
    suspend fun deleteInvestigatorRole(
        investigatorId: String,
        studyId: String
    ): Boolean
}
