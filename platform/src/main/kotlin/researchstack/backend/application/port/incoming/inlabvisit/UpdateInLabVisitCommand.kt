package researchstack.backend.application.port.incoming.inlabvisit

import java.time.LocalDateTime

data class UpdateInLabVisitCommand(
    val picId: String? = null,
    val subjectNumber: String? = null,
    val note: String? = null,
    val filePaths: List<String>? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null
)
