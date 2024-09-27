package researchstack.backend.application.port.outgoing.dashboard

interface DeleteDashboardOutPort {
    suspend fun deleteDashboard(dashboardId: String)
}
