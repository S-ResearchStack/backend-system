package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import researchstack.backend.adapter.outgoing.mongo.entity.dashboard.ChartEntity

interface ChartRepository : ReactiveMongoRepository<ChartEntity, String> {
    fun findAllByDashboardId(dashboardId: String): Flux<ChartEntity>
}
