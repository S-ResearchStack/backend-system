package researchstack.backend.adapter.outgoing.mongo.dashboard

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.ChartRepository
import researchstack.backend.application.port.outgoing.dashboard.DeleteChartOutPort

@Component
class DeleteChartMongoAdapter(
    private val chartRepository: ChartRepository
) : DeleteChartOutPort {
    override suspend fun deleteChart(chartId: String) {
        chartRepository
            .deleteById(chartId)
            .awaitSingleOrNull()
    }
}
