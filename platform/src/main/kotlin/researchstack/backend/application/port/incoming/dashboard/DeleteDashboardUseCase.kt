package researchstack.backend.application.port.incoming.dashboard

interface DeleteDashboardUseCase {
    suspend fun deleteDashboard(studyId: String, dashboardId: String)
}
