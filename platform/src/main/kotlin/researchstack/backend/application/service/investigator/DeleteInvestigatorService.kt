package researchstack.backend.application.service.investigator

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_INVESTIGATOR
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.investigator.DeleteInvestigatorUseCase
import researchstack.backend.application.port.outgoing.casbin.DeleteRoleOutPort
import researchstack.backend.application.port.outgoing.casbin.GetRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.DeleteInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.application.service.permission.CheckPermissionService

@Service
class DeleteInvestigatorService(
    private val getRoleOutPort: GetRoleOutPort,
    private val getInvestigatorOutPort: GetInvestigatorOutPort,
    private val deleteRelationOutPort: DeleteInvestigatorStudyRelationOutPort,
    private val deleteRoleOutPort: DeleteRoleOutPort,
    private val checkPermissionService: CheckPermissionService
) : DeleteInvestigatorUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_INVESTIGATOR])
    override suspend fun deleteInvestigatorRole(
        investigatorId: String,
        @Tenants studyId: String
    ): Boolean {
        val investigator = getInvestigatorOutPort.getInvestigator(investigatorId)
        checkPermissionService.checkPermission(
            studyId,
            getRoleOutPort.getRolesInStudy(investigatorId, studyId),
            ACTION_WRITE
        )
        deleteRelationOutPort.deleteRelationByEmail(investigator.email.value, studyId)
        deleteRoleOutPort.deleteRolesFromStudy(investigatorId, studyId)
        return true
    }
}
