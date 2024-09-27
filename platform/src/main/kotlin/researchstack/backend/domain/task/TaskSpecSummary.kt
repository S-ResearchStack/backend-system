package researchstack.backend.domain.task

import researchstack.backend.enums.TaskType
import java.time.LocalDateTime

data class TaskSpecSummary(
    val id: String,
    val type: TaskType,
    val name: String,
    val time: LocalDateTime
)
