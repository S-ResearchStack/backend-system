package researchstack.backend.application.port.outgoing.inlabvisit

import researchstack.backend.domain.inlabvisit.InLabVisit

interface GetInLabVisitOutPort {
    suspend fun getInLabVisit(inLabVisitId: String): InLabVisit
    suspend fun getInLabVisitList(
        page: Long? = null,
        size: Long? = null
    ): List<InLabVisit>

    suspend fun getInLabVisitListCount(): Long
}
