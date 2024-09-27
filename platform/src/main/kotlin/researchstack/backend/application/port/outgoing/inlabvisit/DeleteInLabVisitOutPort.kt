package researchstack.backend.application.port.outgoing.inlabvisit

interface DeleteInLabVisitOutPort {
    suspend fun deleteInLabVisit(inLabVisitId: String)
}
