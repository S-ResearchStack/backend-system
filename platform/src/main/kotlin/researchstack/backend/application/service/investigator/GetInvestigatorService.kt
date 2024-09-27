package researchstack.backend.application.service.investigator

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_INVESTIGATOR
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.investigator.GetInvestigatorUseCase
import researchstack.backend.application.port.outgoing.casbin.GetRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.domain.investigator.Investigator

@Service
class GetInvestigatorService(
    private val getInvestigatorOutPort: GetInvestigatorOutPort,
    private val getRoleOutPort: GetRoleOutPort
) : GetInvestigatorUseCase {
    override suspend fun getInvestigator(investigatorId: String): Investigator {
        return getInvestigatorOutPort.getInvestigator(investigatorId).apply {
            this.roles = getRoleOutPort.getRoles(id)
        }
    }

    @Role(actions = [ACTION_READ], resources = [RESOURCE_INVESTIGATOR])
    override suspend fun getInvestigators(@Tenants studyId: String): List<Investigator> {
        return getInvestigatorOutPort.getInvestigatorsByStudyId(studyId).onEach {
            it.roles = getRoleOutPort.getRolesInStudy(it.id, studyId)
        }
    }
}
