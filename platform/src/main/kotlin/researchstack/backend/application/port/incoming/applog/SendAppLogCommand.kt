package researchstack.backend.application.port.incoming.applog

import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.service.mapper.AppLogMapper
import researchstack.backend.domain.applog.AppLog

data class SendAppLogCommand(
    val name: String,
    val timestampSeconds: Long,
    val timestampNanos: Long,
    val data: Map<String, String>
) {
    init {
        require(
            !(timestampSeconds == 0L && timestampNanos == 0L)
        ) { ExceptionMessage.EMPTY_TIMESTAMP }
    }

    fun toDomain(): AppLog =
        AppLogMapper.INSTANCE.toDomain(this)
}
