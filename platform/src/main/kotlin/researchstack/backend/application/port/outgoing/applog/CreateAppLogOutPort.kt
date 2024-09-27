package researchstack.backend.application.port.outgoing.applog

import researchstack.backend.domain.applog.AppLog

interface CreateAppLogOutPort {
    suspend fun createAppLog(appLog: AppLog)
}
