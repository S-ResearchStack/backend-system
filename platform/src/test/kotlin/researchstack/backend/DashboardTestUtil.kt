package researchstack.backend

import researchstack.backend.application.port.incoming.dashboard.ChartResponse
import researchstack.backend.application.port.incoming.dashboard.CreateChartCommand
import researchstack.backend.application.port.incoming.dashboard.UpdateChartCommand
import researchstack.backend.domain.dashboard.Chart

class DashboardTestUtil {
    companion object {
        const val chartId = "test-chart-id"
        const val dashboardId = "test-dashboard-id"
        const val studyId = "test-study-id"
        private const val database = "test-database"
        private const val databaseForUpdate = database.plus("-update")
        private const val query = "test-query"
        private const val queryForUpdate = query.plus("-update")
        private const val limit = 1000
        private const val limitForUpdate = 2000
        private const val configName = "test-config-name"
        private const val configNameForUpdate = configName.plus("-update")
        private const val configType = "test-config-type"
        private const val configTypeForUpdate = configType.plus("-update")
        private const val configSpecific = "{\"value\":\"value\",\"category\":\"timestamp\"}"
        private val configSpecificMap = mapOf("value" to "value", "category" to "timestamp")
        private val source = Chart.ChartSource(
            database,
            query,
            Chart.SourceTransform(limit)
        )
        private val config = Chart.ConfigBasic(
            configName,
            configType
        )
        private val sourceForUpdate = Chart.ChartSource(
            databaseForUpdate,
            queryForUpdate,
            Chart.SourceTransform(limitForUpdate)
        )
        private val configForUpdate = Chart.ConfigBasic(
            configNameForUpdate,
            configTypeForUpdate
        )

        fun getCreateChartCommand() = CreateChartCommand(
            source,
            config,
            configSpecificMap
        )

        fun getChartResponse() = ChartResponse(
            chartId,
            dashboardId,
            source,
            config,
            configSpecificMap
        )

        fun getUpdateChartCommand() = UpdateChartCommand(
            sourceForUpdate,
            configForUpdate,
            configSpecificMap
        )

        fun getChartDomain(
            chartId: String? = null
        ) = Chart(
            chartId,
            dashboardId,
            source,
            config,
            configSpecific
        )
    }
}
