package researchstack.backend.application.port.outgoing.task

interface DeleteTaskSpecOutPort {
    suspend fun deleteTaskSpec(taskId: String)

    suspend fun deleteTaskSpec(taskId: String, studyId: String)
}
