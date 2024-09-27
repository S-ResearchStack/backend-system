package researchstack.backend.adapter.outgoing.mongo.applog

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.AppLogRepository
import researchstack.backend.application.port.outgoing.applog.CreateAppLogOutPort
import researchstack.backend.domain.applog.AppLog

@Component
class CreateAppLogMongoAdapter(
    private val appLogRepository: AppLogRepository
) : CreateAppLogOutPort {
    override suspend fun createAppLog(appLog: AppLog) {
        appLogRepository
            .save(appLog.toEntity())
            .awaitSingle()
    }
}
