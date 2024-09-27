package researchstack.backend.application.service.version

import org.springframework.stereotype.Service
import researchstack.backend.adapter.incoming.mapper.version.toResponse
import researchstack.backend.application.port.incoming.version.GetApplicationVersionUseCase
import researchstack.backend.application.port.incoming.version.VersionResponse
import researchstack.backend.application.port.outgoing.version.GetApplicationVersionOutPort

@Service
class GetApplicationVersionService(
    private val outPort: GetApplicationVersionOutPort
) : GetApplicationVersionUseCase {
    override suspend fun getApplicationVersion(): VersionResponse {
        return outPort.getApplicationVersion().toResponse()
    }
}
