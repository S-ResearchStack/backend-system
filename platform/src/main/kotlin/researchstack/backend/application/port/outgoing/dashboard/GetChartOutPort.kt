package researchstack.backend.application.port.outgoing.dashboard

import researchstack.backend.domain.dashboard.Chart

interface GetChartOutPort {
    suspend fun getChart(chartId: String): Chart
    suspend fun getChartList(dashboardId: String): List<Chart>
}
