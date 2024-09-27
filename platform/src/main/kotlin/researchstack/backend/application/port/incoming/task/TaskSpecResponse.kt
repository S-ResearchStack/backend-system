package researchstack.backend.application.port.incoming.task

import org.quartz.CronExpression
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime

data class TaskSpecResponse(
    val id: String,
    val studyId: String,
    val title: String,
    val description: String,
    val schedule: String,
    val createdAt: LocalDateTime? = null,
    val publishedAt: LocalDateTime? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val validMin: Long,
    val duration: String,
    val iconUrl: String? = null,
    val inClinic: Boolean,
    val taskType: TaskType,
    val task: Task
) {
    sealed interface Task

    init {
        require(CronExpression.isValidExpression(schedule)) { ExceptionMessage.INVALID_CRON }
    }
}
