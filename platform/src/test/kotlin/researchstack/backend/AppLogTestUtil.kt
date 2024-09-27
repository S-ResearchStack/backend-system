package researchstack.backend

import researchstack.backend.application.port.incoming.applog.SendAppLogCommand
import researchstack.backend.domain.applog.AppLog
import java.time.LocalDateTime

class AppLogTestUtil {
    companion object {
        const val name = "app log name"
        val now: LocalDateTime = LocalDateTime.now()
        private val timestampSeconds = now.second.toLong()
        private val timestampNanos = now.nano.toLong()
        val data = mapOf(
            "Note" to "Application Started"
        )

        fun createSendAppLogCommand() = SendAppLogCommand(
            name = name,
            timestampSeconds = timestampSeconds,
            timestampNanos = timestampNanos,
            data = data
        )

        fun createDummyAppLog(): AppLog =
            createSendAppLogCommand().toDomain()
    }
}
