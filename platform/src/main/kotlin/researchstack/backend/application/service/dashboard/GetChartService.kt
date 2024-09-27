package researchstack.backend.application.service.dashboard

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_CHART
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.dashboard.ChartResponse
import researchstack.backend.application.port.incoming.dashboard.GetChartUseCase
import researchstack.backend.application.port.outgoing.dashboard.GetChartOutPort
import researchstack.backend.application.service.mapper.toResponse

@Service
class GetChartService(
    private val getChartOutPort: GetChartOutPort
) : GetChartUseCase {
    @Role(actions = [ACTION_READ], resources = [RESOURCE_CHART])
    override suspend fun getChart(@Tenants studyId: String, dashboardId: String, chartId: String): ChartResponse {
        return getChartOutPort.getChart(chartId).toResponse()
    }

    @Role(actions = [ACTION_READ], resources = [RESOURCE_CHART])
    override suspend fun getChartList(@Tenants studyId: String, dashboardId: String): List<ChartResponse> {
        return getChartOutPort.getChartList(dashboardId).map { it.toResponse() }
    }
}
