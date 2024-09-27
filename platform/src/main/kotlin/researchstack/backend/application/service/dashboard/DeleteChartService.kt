package researchstack.backend.application.service.dashboard

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_CHART
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.dashboard.DeleteChartUseCase
import researchstack.backend.application.port.outgoing.dashboard.DeleteChartOutPort

@Service
class DeleteChartService(
    private val deleteChartOutPort: DeleteChartOutPort
) : DeleteChartUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_CHART])
    override suspend fun deleteChart(@Tenants studyId: String, dashboardId: String, chartId: String) {
        deleteChartOutPort.deleteChart(chartId)
    }
}
