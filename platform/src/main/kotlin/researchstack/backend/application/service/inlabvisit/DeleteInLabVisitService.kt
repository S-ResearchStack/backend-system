package researchstack.backend.application.service.inlabvisit

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_IN_LAB_VISIT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.inlabvisit.DeleteInLabVisitUseCase
import researchstack.backend.application.port.outgoing.inlabvisit.DeleteInLabVisitOutPort

@Service
class DeleteInLabVisitService(
    private val deleteInLabVisitOutPort: DeleteInLabVisitOutPort
) : DeleteInLabVisitUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_IN_LAB_VISIT])
    override suspend fun deleteInLabVisit(@Tenants studyId: String, inLabVisitId: String) {
        deleteInLabVisitOutPort.deleteInLabVisit(inLabVisitId)
    }
}
