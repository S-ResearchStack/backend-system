package researchstack.backend.application.service.dashboard

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_DASHBOARD
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.dashboard.GetDashboardUseCase
import researchstack.backend.application.port.outgoing.dashboard.GetDashboardOutPort
import researchstack.backend.domain.dashboard.Dashboard

@Service
class GetDashboardService(
    private val getDashboardOutPort: GetDashboardOutPort
) : GetDashboardUseCase {
    @Role(actions = [ACTION_READ], resources = [RESOURCE_DASHBOARD])
    override suspend fun getDashboard(@Tenants studyId: String, dashboardId: String): Dashboard {
        return getDashboardOutPort.getDashboard(dashboardId)
    }

    @Role(actions = [ACTION_READ], resources = [RESOURCE_DASHBOARD])
    override suspend fun getDashboardList(@Tenants studyId: String): List<Dashboard> {
        return getDashboardOutPort.getDashboardList()
    }
}
