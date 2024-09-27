package researchstack.backend.application.port.outgoing.dashboard

interface DeleteChartOutPort {
    suspend fun deleteChart(chartId: String)
}
