package researchstack.backend.adapter.outgoing.mongo.inlabvisit

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.config.pagination
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository
import researchstack.backend.application.port.outgoing.inlabvisit.GetInLabVisitOutPort
import researchstack.backend.domain.inlabvisit.InLabVisit

@Component
class GetInLabVisitMongoAdapter(
    private val inLabVisitRepository: InLabVisitRepository
) : GetInLabVisitOutPort {
    override suspend fun getInLabVisit(inLabVisitId: String): InLabVisit {
        return inLabVisitRepository
            .findById(inLabVisitId)
            .map { it.toDomain() }
            .awaitSingle()
    }

    override suspend fun getInLabVisitList(page: Long?, size: Long?): List<InLabVisit> {
        return inLabVisitRepository
            .findAll()
            .pagination(page, size)
            .map { it.toDomain() }
            .collectList()
            .awaitSingle()
    }

    override suspend fun getInLabVisitListCount(): Long {
        return inLabVisitRepository
            .findAll()
            .count()
            .awaitSingle()
    }
}
