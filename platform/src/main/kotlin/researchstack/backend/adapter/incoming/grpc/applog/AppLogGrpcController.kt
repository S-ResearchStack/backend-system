package researchstack.backend.adapter.incoming.grpc.applog

import com.google.protobuf.Empty
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import researchstack.backend.application.port.incoming.applog.SendAppLogCommand
import researchstack.backend.application.port.incoming.applog.SendAppLogUseCase
import researchstack.backend.grpc.AppLog
import researchstack.backend.grpc.AppLogServiceGrpcKt

@Component
class AppLogGrpcController(
    private val sendAppLogUseCase: SendAppLogUseCase
) : AppLogServiceGrpcKt.AppLogServiceCoroutineImplBase() {
    private val logger = LoggerFactory.getLogger(AppLogGrpcController::class.java)
    override suspend fun sendAppLog(request: AppLog): Empty {
        logger.info("App Log name: ${request.name}")
        logger.info("App Log timestamp: ${request.timestamp}")
        logger.info("App Log data map: ${request.dataMap}")

        sendAppLogUseCase.sendAppLog(
            SendAppLogCommand(
                name = request.name,
                timestampSeconds = request.timestamp.seconds,
                timestampNanos = request.timestamp.nanos.toLong(),
                data = request.dataMap
            )
        )

        return Empty.newBuilder().build()
    }
}
