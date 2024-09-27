package researchstack.backend.application.service.dashboard

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_CHART
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.dashboard.UpdateChartCommand
import researchstack.backend.application.port.incoming.dashboard.UpdateChartUseCase
import researchstack.backend.application.port.outgoing.dashboard.GetChartOutPort
import researchstack.backend.application.port.outgoing.dashboard.UpdateChartOutPort
import researchstack.backend.application.service.mapper.toDomainConfigSpecific

@Service
class UpdateChartService(
    private val getChartOutPort: GetChartOutPort,
    private val updateChartOutPort: UpdateChartOutPort
) : UpdateChartUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_CHART])
    override suspend fun updateChart(
        @Tenants studyId: String,
        dashboardId: String,
        chartId: String,
        command: UpdateChartCommand
    ) {
        val original = getChartOutPort.getChart(chartId)
        val updated = original.new(
            source = command.source,
            configBasic = command.configBasic,
            configSpecific = command.toDomainConfigSpecific()
        )
        updateChartOutPort.updateChart(updated)
    }
}
