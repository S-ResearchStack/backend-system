package researchstack.backend.application.port.incoming.investigator

import researchstack.backend.domain.investigator.Investigator

interface GetInvestigatorUseCase {
    suspend fun getInvestigator(investigatorId: String): Investigator
    suspend fun getInvestigators(studyId: String): List<Investigator>
}
