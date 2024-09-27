package researchstack.backend.adapter.outgoing.mongo.investigator

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorRepository
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.domain.investigator.Investigator

@Component
class GetInvestigatorMongoAdapter(
    private val investigatorRepository: InvestigatorRepository,
    private val relationRepository: InvestigatorStudyRelationRepository
) : GetInvestigatorOutPort {
    override suspend fun getInvestigator(investigatorId: String): Investigator {
        return investigatorRepository.findById(investigatorId).awaitSingle().toDomain()
    }

    override suspend fun getInvestigatorByEmail(email: String): Investigator {
        return investigatorRepository.findByEmail(email).awaitSingle().toDomain()
    }

    override suspend fun getInvestigatorsByStudyId(studyId: String): List<Investigator> {
        val emails = relationRepository.findAllByStudyId(studyId).map { it.email }.collectList().awaitSingle()
        val investigatorEntities = investigatorRepository.findAllByEmailIn(emails).collectList().awaitSingle()
        return investigatorEntities.map { it.toDomain() }
    }
}
