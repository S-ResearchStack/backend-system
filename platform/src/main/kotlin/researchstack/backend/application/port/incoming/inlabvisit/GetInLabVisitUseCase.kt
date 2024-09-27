package researchstack.backend.application.port.incoming.inlabvisit

import researchstack.backend.application.port.incoming.common.PaginationCommand

interface GetInLabVisitUseCase {
    suspend fun getInLabVisitList(
        studyId: String,
        paginationCommand: PaginationCommand? = null
    ): GetInLabVisitListResponse
}
