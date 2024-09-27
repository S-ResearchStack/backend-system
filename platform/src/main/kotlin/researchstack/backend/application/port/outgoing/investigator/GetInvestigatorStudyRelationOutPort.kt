package researchstack.backend.application.port.outgoing.investigator

import researchstack.backend.domain.investigator.InvestigatorStudyRelation

interface GetInvestigatorStudyRelationOutPort {
    suspend fun getRelationsByEmail(email: String): List<InvestigatorStudyRelation>
}
