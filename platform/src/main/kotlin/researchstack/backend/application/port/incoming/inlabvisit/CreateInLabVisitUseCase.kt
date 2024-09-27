package researchstack.backend.application.port.incoming.inlabvisit

interface CreateInLabVisitUseCase {
    suspend fun createInLabVisit(
        studyId: String,
        investigatorId: String,
        command: CreateInLabVisitCommand
    ): CreateInLabVisitResponse
}
