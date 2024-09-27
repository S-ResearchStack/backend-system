package researchstack.backend.application.port.outgoing.investigator

import researchstack.backend.domain.investigator.Investigator

interface GetInvestigatorOutPort {
    suspend fun getInvestigator(investigatorId: String): Investigator
    suspend fun getInvestigatorByEmail(email: String): Investigator
    suspend fun getInvestigatorsByStudyId(studyId: String): List<Investigator>
}
