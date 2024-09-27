package researchstack.backend.application.port.incoming.task

interface CreateTaskSpecUseCase {
    suspend fun createTaskSpec(command: CreateTaskSpecCommand, studyId: String)
}
