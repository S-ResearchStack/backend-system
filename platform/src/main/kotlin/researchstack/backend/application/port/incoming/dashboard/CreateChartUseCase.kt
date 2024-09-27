package researchstack.backend.application.port.incoming.dashboard

interface CreateChartUseCase {
    suspend fun createChart(
        studyId: String,
        dashboardId: String,
        command: CreateChartCommand
    ): String
}
