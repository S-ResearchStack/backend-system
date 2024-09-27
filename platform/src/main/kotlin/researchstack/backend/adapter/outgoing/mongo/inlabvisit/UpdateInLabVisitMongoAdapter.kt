package researchstack.backend.adapter.outgoing.mongo.inlabvisit

import com.linecorp.armeria.server.ServiceRequestContext
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository
import researchstack.backend.application.port.outgoing.inlabvisit.UpdateInLabVisitOutPort
import researchstack.backend.domain.inlabvisit.InLabVisit

@Component
class UpdateInLabVisitMongoAdapter(
    private val inLabVisitRepository: InLabVisitRepository
) : UpdateInLabVisitOutPort {
    override suspend fun updateInLabVisit(inLabVisit: InLabVisit) {
        val contextAwareScheduler = Schedulers.fromExecutor(ServiceRequestContext.current().blockingTaskExecutor())
        if (inLabVisit.id == null) {
            throw IllegalArgumentException("in lab visit id is null")
        }
        inLabVisitRepository
            .findById(inLabVisit.id)
            .publishOn(contextAwareScheduler)
            .flatMap {
                inLabVisitRepository.save(inLabVisit.toEntity())
            }
            .awaitSingle()
    }
}
