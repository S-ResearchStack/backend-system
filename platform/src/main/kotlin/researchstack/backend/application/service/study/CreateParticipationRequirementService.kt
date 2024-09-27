package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_PARTICIPATION_REQUIREMENT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementCommand
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementUseCase
import researchstack.backend.application.port.outgoing.study.CreateParticipationRequirementOutPort

@Service
class CreateParticipationRequirementService(
    private val createParticipationRequirementOutPort: CreateParticipationRequirementOutPort
) : CreateParticipationRequirementUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_PARTICIPATION_REQUIREMENT])
    override suspend fun create(command: CreateParticipationRequirementCommand, @Tenants studyId: String) {
        createParticipationRequirementOutPort.create(command.toDomain(), studyId)
    }
}
