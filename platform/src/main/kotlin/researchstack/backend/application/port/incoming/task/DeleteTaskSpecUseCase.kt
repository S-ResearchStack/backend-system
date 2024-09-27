package researchstack.backend.application.port.incoming.task

interface DeleteTaskSpecUseCase {
    suspend fun deleteTaskSpec(taskId: String, studyId: String)
}
