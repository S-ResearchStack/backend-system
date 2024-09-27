package researchstack.backend.application.service.dashboard

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_CHART
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.dashboard.CreateChartCommand
import researchstack.backend.application.port.incoming.dashboard.CreateChartUseCase
import researchstack.backend.application.port.outgoing.dashboard.CreateChartOutPort
import researchstack.backend.application.service.mapper.toDomain

@Service
class CreateChartService(
    private val createChartOutPort: CreateChartOutPort
) : CreateChartUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_CHART])
    override suspend fun createChart(
        @Tenants studyId: String,
        dashboardId: String,
        command: CreateChartCommand
    ): String {
        return createChartOutPort.createChart(command.toDomain(studyId, dashboardId))
    }
}
