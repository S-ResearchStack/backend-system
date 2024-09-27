package researchstack.backend.application.port.incoming.dashboard

interface UpdateChartUseCase {
    suspend fun updateChart(
        studyId: String,
        dashboardId: String,
        chartId: String,
        command: UpdateChartCommand
    )
}
