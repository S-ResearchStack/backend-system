package researchstack.backend.application.port.incoming.task

import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.TaskStatus
import java.time.LocalDateTime

interface GetTaskSpecUseCase {
    suspend fun getTaskSpec(studyId: String, taskId: String): TaskSpecResponse
    suspend fun getTaskSpecs(studyId: String, status: TaskStatus? = null): List<TaskSpecResponse>
    suspend fun getAllTaskSpecs(subjectId: Subject.SubjectId): List<TaskSpecResponse>
    suspend fun getAllNewTaskSpecs(subjectId: Subject.SubjectId, lastSyncTime: LocalDateTime): List<TaskSpecResponse>
}
