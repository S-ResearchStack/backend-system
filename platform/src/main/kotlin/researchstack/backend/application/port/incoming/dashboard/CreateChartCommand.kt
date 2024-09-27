package researchstack.backend.application.port.incoming.dashboard

import researchstack.backend.domain.dashboard.Chart

data class CreateChartCommand(
    val source: Chart.ChartSource,
    val configBasic: Chart.ConfigBasic,
    val configSpecific: Map<String, Any>
)
