package researchstack.backend.adapter.outgoing.mongo.investigator

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.investigator.AddInvestigatorStudyRelationOutPort
import researchstack.backend.domain.investigator.InvestigatorStudyRelation

@Component
class AddInvestigatorStudyRelationMongoAdapter(
    private val relationRepository: InvestigatorStudyRelationRepository
) : AddInvestigatorStudyRelationOutPort {
    override suspend fun addRelation(relation: InvestigatorStudyRelation): InvestigatorStudyRelation {
        if (relationRepository.existsByEmailAndStudyId(relation.email.value, relation.studyId).awaitSingle()) {
            throw AlreadyExistsException()
        }
        return relationRepository.insert(relation.toEntity()).awaitSingle().toDomain()
    }
}
