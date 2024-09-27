package researchstack.backend.adapter.outgoing.mongo.investigator

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.application.port.outgoing.investigator.DeleteInvestigatorStudyRelationOutPort

@Component
class DeleteInvestigatorStudyRelationMongoAdapter(
    private val relationRepository: InvestigatorStudyRelationRepository
) : DeleteInvestigatorStudyRelationOutPort {
    override suspend fun deleteRelationByEmail(email: String, studyId: String) {
        relationRepository.deleteByEmailAndStudyId(email, studyId).awaitFirstOrDefault(null)
    }

    override suspend fun deleteByStudyId(studyId: String) {
        relationRepository.deleteByStudyId(studyId).awaitFirstOrDefault(null)
    }
}
