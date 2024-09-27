package researchstack.backend.application.port.incoming.investigator

import researchstack.backend.domain.investigator.Investigator

interface RegisterInvestigatorUseCase {
    suspend fun registerInvestigator(
        investigatorId: String,
        email: String,
        command: RegisterInvestigatorCommand
    ): Investigator
}
