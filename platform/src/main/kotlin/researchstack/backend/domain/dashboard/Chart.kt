package researchstack.backend.domain.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class Chart(
    val id: String? = null,
    val dashboardId: String,
    val source: ChartSource,
    val configBasic: ConfigBasic,
    val configSpecific: String
) {
    @Serializable
    data class ChartSource(
        val database: String,
        val query: String,
        val transform: SourceTransform
    )

    @Serializable
    data class SourceTransform(val limit: Int)

    @Serializable
    data class ConfigBasic(
        val name: String,
        val type: String
    )

    fun new(
        id: String? = null,
        dashboardId: String? = null,
        source: ChartSource? = null,
        configBasic: ConfigBasic? = null,
        configSpecific: String? = null
    ): Chart = Chart(
        id = id ?: this.id,
        dashboardId = dashboardId ?: this.dashboardId,
        source = source ?: this.source,
        configBasic = configBasic ?: this.configBasic,
        configSpecific = configSpecific ?: this.configSpecific
    )
}
