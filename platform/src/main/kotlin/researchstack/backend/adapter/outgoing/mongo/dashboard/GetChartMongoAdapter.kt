package researchstack.backend.adapter.outgoing.mongo.dashboard

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.ChartRepository
import researchstack.backend.application.port.outgoing.dashboard.GetChartOutPort
import researchstack.backend.domain.dashboard.Chart

@Component
class GetChartMongoAdapter(
    private val chartRepository: ChartRepository
) : GetChartOutPort {
    override suspend fun getChart(chartId: String): Chart {
        return chartRepository
            .findById(chartId)
            .map { it.toDomain() }
            .awaitSingle()
    }

    override suspend fun getChartList(dashboardId: String): List<Chart> {
        return chartRepository
            .findAllByDashboardId(dashboardId)
            .map { it.toDomain() }
            .collectList()
            .awaitSingle()
    }
}
