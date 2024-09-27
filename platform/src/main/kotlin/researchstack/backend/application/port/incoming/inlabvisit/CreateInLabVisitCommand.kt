package researchstack.backend.application.port.incoming.inlabvisit

import java.time.LocalDateTime

data class CreateInLabVisitCommand(
    val picId: String,
    val subjectNumber: String,
    val note: String? = null,
    val filePaths: List<String>? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)
