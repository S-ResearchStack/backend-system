package researchstack.backend.application.port.outgoing.task

import researchstack.backend.domain.task.TaskSpec

interface UpdateTaskSpecOutPort {
    suspend fun updateTaskSpec(taskSpec: TaskSpec)
}
