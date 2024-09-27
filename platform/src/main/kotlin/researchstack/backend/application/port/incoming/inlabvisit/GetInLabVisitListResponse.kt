package researchstack.backend.application.port.incoming.inlabvisit

import researchstack.backend.domain.inlabvisit.InLabVisit

data class GetInLabVisitListResponse(
    val list: List<InLabVisit>,
    val totalCount: Number
)
