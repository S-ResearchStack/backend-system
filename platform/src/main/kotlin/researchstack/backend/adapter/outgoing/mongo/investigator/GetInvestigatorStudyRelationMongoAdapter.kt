package researchstack.backend.adapter.outgoing.mongo.investigator

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorStudyRelationOutPort
import researchstack.backend.domain.investigator.InvestigatorStudyRelation

@Component
class GetInvestigatorStudyRelationMongoAdapter(
    private val relationRepository: InvestigatorStudyRelationRepository
) : GetInvestigatorStudyRelationOutPort {

    override suspend fun getRelationsByEmail(email: String): List<InvestigatorStudyRelation> {
        return relationRepository.findAllByEmail(email).collectList().awaitSingle().map { it.toDomain() }
    }
}
