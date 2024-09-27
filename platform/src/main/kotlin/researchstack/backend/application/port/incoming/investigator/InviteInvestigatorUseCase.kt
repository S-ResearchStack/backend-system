package researchstack.backend.application.port.incoming.investigator

interface InviteInvestigatorUseCase {
    suspend fun inviteInvestigator(
        inviterInvestigatorId: String,
        studyId: String,
        role: String,
        command: InviteInvestigatorCommand
    ): Boolean
}
