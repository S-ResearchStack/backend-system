package researchstack.backend.application.port.incoming.investigator

interface UpdateInvestigatorUseCase {
    suspend fun updateInvestigator(investigatorId: String, email: String, command: UpdateInvestigatorCommand)
    suspend fun updateInvestigatorRole(investigatorId: String, studyId: String, role: String): String
}
