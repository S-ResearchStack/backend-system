package researchstack.backend.application.port.incoming.dashboard

import researchstack.backend.domain.dashboard.Dashboard

interface GetDashboardUseCase {
    suspend fun getDashboard(studyId: String, dashboardId: String): Dashboard
    suspend fun getDashboardList(studyId: String): List<Dashboard>
}
