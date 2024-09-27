package researchstack.backend.application.port.outgoing.dashboard

import researchstack.backend.domain.dashboard.Dashboard

interface GetDashboardOutPort {
    suspend fun getDashboard(dashboardId: String): Dashboard
    suspend fun getDashboardList(): List<Dashboard>
}
