package researchstack.backend.application.port.outgoing.dashboard

import researchstack.backend.domain.dashboard.Dashboard

interface CreateDashboardOutPort {
    suspend fun createDashboard(dashboard: Dashboard): String
}
