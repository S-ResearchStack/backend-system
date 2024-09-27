package researchstack.backend.application.port.incoming.dashboard

interface GetChartUseCase {
    suspend fun getChart(studyId: String, dashboardId: String, chartId: String): ChartResponse
    suspend fun getChartList(studyId: String, dashboardId: String): List<ChartResponse>
}
