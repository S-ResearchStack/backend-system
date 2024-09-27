package researchstack.backend.application.port.outgoing.investigator

import researchstack.backend.domain.investigator.InvestigatorStudyRelation

interface AddInvestigatorStudyRelationOutPort {
    suspend fun addRelation(relation: InvestigatorStudyRelation): InvestigatorStudyRelation
}
