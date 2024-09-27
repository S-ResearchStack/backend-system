package researchstack.backend.application.port.outgoing.dashboard

import researchstack.backend.domain.dashboard.Chart

interface UpdateChartOutPort {
    suspend fun updateChart(chart: Chart)
}
