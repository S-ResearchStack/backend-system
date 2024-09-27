package researchstack.backend.application.port.incoming.dashboard

import researchstack.backend.domain.dashboard.Chart

data class ChartResponse(
    val id: String? = null,
    val dashboardId: String,
    val source: Chart.ChartSource,
    val configBasic: Chart.ConfigBasic,
    val configSpecific: Map<String, Any>
)
