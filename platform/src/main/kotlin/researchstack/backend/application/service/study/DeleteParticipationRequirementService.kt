package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_PARTICIPATION_REQUIREMENT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.study.DeleteParticipationRequirementUseCase
import researchstack.backend.application.port.outgoing.study.DeleteParticipationRequirementOutPort

@Service
class DeleteParticipationRequirementService(
    private val deleteParticipationRequirementOutPort: DeleteParticipationRequirementOutPort
) : DeleteParticipationRequirementUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_PARTICIPATION_REQUIREMENT])
    override suspend fun delete(@Tenants studyId: String) {
        deleteParticipationRequirementOutPort.delete(studyId)
    }
}
