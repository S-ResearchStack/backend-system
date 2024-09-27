package researchstack.backend.application.port.incoming.dashboard

interface DeleteChartUseCase {
    suspend fun deleteChart(studyId: String, dashboardId: String, chartId: String)
}
