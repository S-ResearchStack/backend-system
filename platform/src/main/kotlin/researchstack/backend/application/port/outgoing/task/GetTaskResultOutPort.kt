package researchstack.backend.application.port.outgoing.task

import researchstack.backend.domain.task.TaskResult
import java.time.LocalDateTime

interface GetTaskResultOutPort {
    suspend fun getTaskResultList(
        studyId: String,
        subjectId: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<TaskResult>
}
