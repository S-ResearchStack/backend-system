package researchstack.backend.application.service

import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.applog.SendAppLogCommand
import researchstack.backend.application.port.incoming.applog.SendAppLogUseCase
import researchstack.backend.application.port.outgoing.applog.CreateAppLogOutPort

@Service
class AppLogService(
    private val createAppLogOutPort: CreateAppLogOutPort
) : SendAppLogUseCase {
    override suspend fun sendAppLog(sendAppLogCommand: SendAppLogCommand) {
        val appLog = sendAppLogCommand.toDomain()

        createAppLogOutPort.createAppLog(appLog)
    }
}
