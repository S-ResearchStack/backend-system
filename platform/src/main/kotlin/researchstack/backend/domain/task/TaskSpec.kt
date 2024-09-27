package researchstack.backend.domain.task

import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime
import java.util.UUID

data class TaskSpec(
    val id: String = UUID.randomUUID().toString(),
    val studyId: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val schedule: String,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val publishedAt: LocalDateTime?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val validMin: Long,
    val duration: String,
    val iconUrl: String? = null,
    val inClinic: Boolean,
    val taskType: TaskType,
    val task: Task
) {
    sealed interface Task
}
