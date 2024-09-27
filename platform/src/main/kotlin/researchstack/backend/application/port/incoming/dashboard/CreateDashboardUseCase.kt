package researchstack.backend.application.port.incoming.dashboard

interface CreateDashboardUseCase {
    suspend fun createDashboard(studyId: String, command: CreateDashboardCommand): String
}
