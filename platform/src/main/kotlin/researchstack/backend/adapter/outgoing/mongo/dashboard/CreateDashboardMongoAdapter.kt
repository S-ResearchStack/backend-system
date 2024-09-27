package researchstack.backend.adapter.outgoing.mongo.dashboard

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.DashboardRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.InternalServerException
import researchstack.backend.application.port.outgoing.dashboard.CreateDashboardOutPort
import researchstack.backend.domain.dashboard.Dashboard

@Component
class CreateDashboardMongoAdapter(
    private val dashboardRepository: DashboardRepository
) : CreateDashboardOutPort {
    override suspend fun createDashboard(dashboard: Dashboard): String {
        val dashboardEntity = dashboardRepository
            .insert(dashboard.toEntity())
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
        if (dashboardEntity.id == null) {
            throw InternalServerException("dashboard id is null")
        }
        return dashboardEntity.id
    }
}
