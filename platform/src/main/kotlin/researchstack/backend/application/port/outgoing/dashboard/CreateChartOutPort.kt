package researchstack.backend.application.port.outgoing.dashboard

import researchstack.backend.domain.dashboard.Chart

interface CreateChartOutPort {
    suspend fun createChart(chart: Chart): String
}
