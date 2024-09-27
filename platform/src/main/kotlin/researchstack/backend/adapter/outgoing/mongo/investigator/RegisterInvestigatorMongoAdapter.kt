package researchstack.backend.adapter.outgoing.mongo.investigator

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorRepository
import researchstack.backend.application.port.outgoing.investigator.RegisterInvestigatorOutPort
import researchstack.backend.domain.investigator.Investigator

@Component
class RegisterInvestigatorMongoAdapter(
    private val investigatorRepository: InvestigatorRepository
) : RegisterInvestigatorOutPort {
    override suspend fun registerInvestigator(investigator: Investigator): Investigator {
        return investigatorRepository.insert(investigator.toEntity()).awaitSingle().toDomain()
    }
}
