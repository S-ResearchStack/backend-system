package researchstack.backend.application.port.incoming.inlabvisit

interface UpdateInLabVisitUseCase {
    suspend fun updateInLabVisit(studyId: String, inLabVisitId: String, command: UpdateInLabVisitCommand)
}
