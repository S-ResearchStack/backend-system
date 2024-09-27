package researchstack.backend.application.port.outgoing.investigator

import researchstack.backend.domain.investigator.Investigator

interface UpdateInvestigatorOutPort {
    suspend fun updateInvestigator(investigator: Investigator)
}
