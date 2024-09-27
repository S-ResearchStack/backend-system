package researchstack.backend.application.port.outgoing.inlabvisit

import researchstack.backend.domain.inlabvisit.InLabVisit

interface UpdateInLabVisitOutPort {
    suspend fun updateInLabVisit(inLabVisit: InLabVisit)
}
