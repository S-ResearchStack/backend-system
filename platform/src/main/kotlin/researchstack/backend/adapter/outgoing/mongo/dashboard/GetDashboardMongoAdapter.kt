package researchstack.backend.adapter.outgoing.mongo.dashboard

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.DashboardRepository
import researchstack.backend.application.port.outgoing.dashboard.GetDashboardOutPort
import researchstack.backend.domain.dashboard.Dashboard

@Component
class GetDashboardMongoAdapter(
    private val dashboardRepository: DashboardRepository
) : GetDashboardOutPort {
    override suspend fun getDashboard(dashboardId: String): Dashboard {
        return dashboardRepository
            .findById(dashboardId)
            .map { it.toDomain() }
            .awaitSingle()
    }

    override suspend fun getDashboardList(): List<Dashboard> {
        return dashboardRepository
            .findAll()
            .map { it.toDomain() }
            .collectList()
            .awaitSingle()
    }
}
