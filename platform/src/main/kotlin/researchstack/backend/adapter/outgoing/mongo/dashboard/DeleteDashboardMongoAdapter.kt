package researchstack.backend.adapter.outgoing.mongo.dashboard

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.DashboardRepository
import researchstack.backend.application.port.outgoing.dashboard.DeleteDashboardOutPort

@Component
class DeleteDashboardMongoAdapter(
    private val dashboardRepository: DashboardRepository
) : DeleteDashboardOutPort {
    override suspend fun deleteDashboard(dashboardId: String) {
        dashboardRepository
            .deleteById(dashboardId)
            .awaitSingleOrNull()
    }
}
