package researchstack.backend.adapter.outgoing.mongo.dashboard

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ChartRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.InternalServerException
import researchstack.backend.application.port.outgoing.dashboard.CreateChartOutPort
import researchstack.backend.domain.dashboard.Chart

@Component
class CreateChartMongoAdapter(
    private val chartRepository: ChartRepository
) : CreateChartOutPort {
    override suspend fun createChart(chart: Chart): String {
        val chartEntity = chartRepository
            .insert(chart.toEntity())
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
        if (chartEntity.id == null) {
            throw InternalServerException("chart id is null")
        }
        return chartEntity.id
    }
}
