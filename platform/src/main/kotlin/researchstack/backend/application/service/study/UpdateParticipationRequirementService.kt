package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_PARTICIPATION_REQUIREMENT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.study.UpdateParticipationRequirementCommand
import researchstack.backend.application.port.incoming.study.UpdateParticipationRequirementUseCase
import researchstack.backend.application.port.outgoing.study.UpdateParticipationRequirementOutPort

@Service
class UpdateParticipationRequirementService(
    private val updateParticipationRequirementOutPort: UpdateParticipationRequirementOutPort
) : UpdateParticipationRequirementUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_PARTICIPATION_REQUIREMENT])
    override suspend fun update(@Tenants studyId: String, command: UpdateParticipationRequirementCommand) {
        updateParticipationRequirementOutPort.update(studyId, command.toDomain())
    }
}
