package researchstack.backend.application.port.outgoing.inlabvisit

import researchstack.backend.domain.inlabvisit.InLabVisit

interface CreateInLabVisitOutPort {
    suspend fun createInLabVisit(inLabVisit: InLabVisit): InLabVisit
}
