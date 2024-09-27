package researchstack.backend.adapter.outgoing.mongo.inlabvisit

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository
import researchstack.backend.application.port.outgoing.inlabvisit.DeleteInLabVisitOutPort

@Component
class DeleteInLabVisitMongoAdapter(
    private val inLabVisitRepository: InLabVisitRepository
) : DeleteInLabVisitOutPort {
    override suspend fun deleteInLabVisit(inLabVisitId: String) {
        inLabVisitRepository.deleteById(inLabVisitId).awaitFirstOrDefault(null)
    }
}
