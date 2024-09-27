package researchstack.backend.adapter.outgoing.mongo.investigator

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.investigator.UpdateInvestigatorOutPort
import researchstack.backend.domain.investigator.Investigator

@Component
class UpdateInvestigatorMongoAdapter(
    private val investigatorRepository: InvestigatorRepository
) : UpdateInvestigatorOutPort {
    override suspend fun updateInvestigator(investigator: Investigator) {
        investigatorRepository.findById(investigator.id)
            .flatMap {
                investigatorRepository.save(investigator.toEntity())
            }
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
