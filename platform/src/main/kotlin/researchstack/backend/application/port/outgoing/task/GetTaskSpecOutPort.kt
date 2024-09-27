package researchstack.backend.application.port.outgoing.task

import researchstack.backend.domain.subject.Subject
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.enums.TaskStatus
import java.time.LocalDateTime

interface GetTaskSpecOutPort {
    suspend fun getTaskSpec(taskId: String): TaskSpec
    suspend fun getTaskSpecs(studyId: String, status: TaskStatus? = null): List<TaskSpec>
    suspend fun getAllTaskSpecs(subjectId: Subject.SubjectId): List<TaskSpec>
    suspend fun getAllNewTaskSpecs(subjectId: Subject.SubjectId, lastSyncTime: LocalDateTime): List<TaskSpec>
}
