package researchstack.backend.application.service.inlabvisit

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_IN_LAB_VISIT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.common.PaginationCommand
import researchstack.backend.application.port.incoming.inlabvisit.GetInLabVisitListResponse
import researchstack.backend.application.port.incoming.inlabvisit.GetInLabVisitUseCase
import researchstack.backend.application.port.outgoing.inlabvisit.GetInLabVisitOutPort

@Service
class GetInLabVisitService(
    private val getInLabVisitOutPort: GetInLabVisitOutPort
) : GetInLabVisitUseCase {
    @Role(actions = [ACTION_READ], resources = [RESOURCE_IN_LAB_VISIT])
    override suspend fun getInLabVisitList(
        @Tenants studyId: String,
        paginationCommand: PaginationCommand?
    ): GetInLabVisitListResponse {
        val list = getInLabVisitOutPort.getInLabVisitList(
            page = paginationCommand?.page,
            size = paginationCommand?.size
        )
        val totalCount = getInLabVisitOutPort.getInLabVisitListCount()
        return GetInLabVisitListResponse(list, totalCount)
    }
}
