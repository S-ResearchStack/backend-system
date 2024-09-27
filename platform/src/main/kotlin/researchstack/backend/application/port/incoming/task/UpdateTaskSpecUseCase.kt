package researchstack.backend.application.port.incoming.task

interface UpdateTaskSpecUseCase {
    suspend fun updateTaskSpec(command: UpdateTaskSpecCommand, taskId: String, studyId: String)
}
