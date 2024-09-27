package researchstack.backend.adapter.outgoing.mongo.investigator

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.application.port.outgoing.investigator.UpdateInvestigatorStudyRelationOutPort
import researchstack.backend.domain.investigator.InvestigatorStudyRelation

@Component
class UpdateInvestigatorStudyRelationMongoAdapter(
    private val relationRepository: InvestigatorStudyRelationRepository
) : UpdateInvestigatorStudyRelationOutPort {
    override suspend fun updateRelation(email: String, studyId: String, role: String): InvestigatorStudyRelation {
        return relationRepository.findByEmailAndStudyId(email, studyId).awaitSingle().apply {
            this.role = role
            relationRepository.save(this).awaitSingle()
        }.toDomain()
    }
}
