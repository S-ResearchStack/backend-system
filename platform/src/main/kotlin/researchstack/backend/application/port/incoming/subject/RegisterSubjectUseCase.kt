package researchstack.backend.application.port.incoming.subject

interface RegisterSubjectUseCase {
    suspend fun registerSubject(command: RegisterSubjectCommand)
}
