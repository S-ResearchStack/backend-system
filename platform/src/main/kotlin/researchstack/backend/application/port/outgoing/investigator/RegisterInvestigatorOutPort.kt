package researchstack.backend.application.port.outgoing.investigator

import researchstack.backend.domain.investigator.Investigator

interface RegisterInvestigatorOutPort {
    suspend fun registerInvestigator(investigator: Investigator): Investigator
}
