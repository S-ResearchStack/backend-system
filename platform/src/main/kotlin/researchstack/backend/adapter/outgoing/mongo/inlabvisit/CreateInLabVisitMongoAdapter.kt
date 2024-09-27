package researchstack.backend.adapter.outgoing.mongo.inlabvisit

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.inlabvisit.CreateInLabVisitOutPort
import researchstack.backend.domain.inlabvisit.InLabVisit

@Component
class CreateInLabVisitMongoAdapter(
    private val inLabVisitRepository: InLabVisitRepository
) : CreateInLabVisitOutPort {
    override suspend fun createInLabVisit(inLabVisit: InLabVisit): InLabVisit {
        return inLabVisitRepository
            .insert(inLabVisit.toEntity())
            .map { it.toDomain() }
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
