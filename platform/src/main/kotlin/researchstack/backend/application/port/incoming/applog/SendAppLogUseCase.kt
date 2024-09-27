package researchstack.backend.application.port.incoming.applog

interface SendAppLogUseCase {
    suspend fun sendAppLog(sendAppLogCommand: SendAppLogCommand)
}
