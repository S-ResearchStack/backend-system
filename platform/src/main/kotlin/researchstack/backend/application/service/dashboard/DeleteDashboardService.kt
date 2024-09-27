package researchstack.backend.application.service.dashboard

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_DASHBOARD
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.dashboard.DeleteDashboardUseCase
import researchstack.backend.application.port.outgoing.dashboard.DeleteDashboardOutPort

@Service
class DeleteDashboardService(
    private val deleteDashboardOutPort: DeleteDashboardOutPort
) : DeleteDashboardUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_DASHBOARD])
    override suspend fun deleteDashboard(@Tenants studyId: String, dashboardId: String) {
        deleteDashboardOutPort.deleteDashboard(dashboardId)
    }
}
