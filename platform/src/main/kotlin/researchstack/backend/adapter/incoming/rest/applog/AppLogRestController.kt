package researchstack.backend.adapter.incoming.rest.applog

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import researchstack.backend.application.port.incoming.applog.SendAppLogCommand
import researchstack.backend.application.port.incoming.applog.SendAppLogUseCase

@Component
class AppLogRestController(
    private val sendAppLogUseCase: SendAppLogUseCase
) {
    private val logger = LoggerFactory.getLogger(AppLogRestController::class.java)

    @Post("/app-log")
    suspend fun sendAppLog(@RequestObject command: SendAppLogCommand): HttpResponse {
        logger.info("App Log name: ${command.name}")
        logger.info("App Log timestamp: second:${command.timestampSeconds}, nano:${command.timestampNanos}")
        logger.info("App Log data map: ${command.data}")

        sendAppLogUseCase.sendAppLog(
            SendAppLogCommand(
                name = command.name,
                timestampSeconds = command.timestampSeconds,
                timestampNanos = command.timestampNanos,
                data = command.data
            )
        )
        return HttpResponse.of(HttpStatus.OK)
    }
}
