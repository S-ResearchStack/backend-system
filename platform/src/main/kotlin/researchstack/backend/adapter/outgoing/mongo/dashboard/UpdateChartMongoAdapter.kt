package researchstack.backend.adapter.outgoing.mongo.dashboard

import com.linecorp.armeria.server.ServiceRequestContext
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ChartRepository
import researchstack.backend.application.port.outgoing.dashboard.UpdateChartOutPort
import researchstack.backend.domain.dashboard.Chart

@Component
class UpdateChartMongoAdapter(
    private val chartRepository: ChartRepository
) : UpdateChartOutPort {
    override suspend fun updateChart(chart: Chart) {
        val contextAwareScheduler = Schedulers.fromExecutor(ServiceRequestContext.current().blockingTaskExecutor())
        if (chart.id == null) {
            throw IllegalArgumentException("chart id is null")
        }
        chartRepository
            .findById(chart.id)
            .publishOn(contextAwareScheduler)
            .flatMap {
                chartRepository.save(chart.toEntity())
            }
            .awaitSingle()
    }
}
