package researchstack.backend.application.port.outgoing.task

import researchstack.backend.domain.task.TaskSpec

interface CreateTaskSpecOutPort {
    suspend fun createTaskSpec(taskSpec: TaskSpec)
}
