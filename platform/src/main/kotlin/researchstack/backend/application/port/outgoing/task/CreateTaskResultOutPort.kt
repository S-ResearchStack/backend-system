package researchstack.backend.application.port.outgoing.task

import researchstack.backend.domain.task.TaskResult

interface CreateTaskResultOutPort {
    suspend fun createTaskResult(subjectId: String, taskResult: TaskResult)
}
