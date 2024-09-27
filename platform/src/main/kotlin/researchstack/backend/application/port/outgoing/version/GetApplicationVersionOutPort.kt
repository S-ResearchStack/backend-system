package researchstack.backend.application.port.outgoing.version

import researchstack.backend.domain.version.Version

interface GetApplicationVersionOutPort {
    suspend fun getApplicationVersion(): Version
}
