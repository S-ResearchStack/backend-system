package researchstack.backend.application.port.incoming.task

import org.quartz.CronExpression
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.service.mapper.TaskSpecMapper
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime

data class UpdateTaskSpecCommand(
    val title: String,
    val description: String,
    val status: TaskStatus,
    val schedule: String,
    val createdAt: LocalDateTime?,
    val publishedAt: LocalDateTime?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val validMin: Long,
    val duration: String,
    val iconUrl: String?,
    val inClinic: Boolean = false,
    val taskType: TaskType,
    val task: Map<String, Any>
) {
    init {
        require(CronExpression.isValidExpression(schedule)) { ExceptionMessage.INVALID_CRON }
    }

    fun toDomain(taskId: String, studyId: String): TaskSpec = TaskSpecMapper.INSTANCE.toDomain(this, taskId, studyId)
}
