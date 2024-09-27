package researchstack.backend.application.port.outgoing.investigator

import researchstack.backend.domain.investigator.InvestigatorStudyRelation

interface UpdateInvestigatorStudyRelationOutPort {
    suspend fun updateRelation(email: String, studyId: String, role: String): InvestigatorStudyRelation
}
