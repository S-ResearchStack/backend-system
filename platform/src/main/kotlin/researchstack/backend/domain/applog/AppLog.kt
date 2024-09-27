package researchstack.backend.domain.applog

import java.time.LocalDateTime

data class AppLog(
    val name: String,
    val timestamp: LocalDateTime,
    val data: Map<String, String>
)
