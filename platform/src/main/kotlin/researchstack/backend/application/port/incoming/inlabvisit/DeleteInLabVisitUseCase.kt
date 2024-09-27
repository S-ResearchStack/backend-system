package researchstack.backend.application.port.incoming.inlabvisit

interface DeleteInLabVisitUseCase {
    suspend fun deleteInLabVisit(studyId: String, inLabVisitId: String)
}
