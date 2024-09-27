package researchstack.backend.application.port.incoming.version

interface GetApplicationVersionUseCase {
    suspend fun getApplicationVersion(): VersionResponse
}
