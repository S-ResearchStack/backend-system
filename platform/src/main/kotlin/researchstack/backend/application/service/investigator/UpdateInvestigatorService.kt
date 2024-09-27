package researchstack.backend.application.service.investigator

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PUBLIC_TENANT
import researchstack.backend.adapter.role.Resources
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorUseCase
import researchstack.backend.application.port.outgoing.casbin.UpdateRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.application.port.outgoing.investigator.UpdateInvestigatorOutPort
import researchstack.backend.application.port.outgoing.investigator.UpdateInvestigatorStudyRelationOutPort
import researchstack.backend.application.service.mapper.toDomain

@Service
class UpdateInvestigatorService(
    private val updateInvestigatorOutPort: UpdateInvestigatorOutPort,
    private val updateRoleOutPort: UpdateRoleOutPort,
    private val getInvestigatorOutPort: GetInvestigatorOutPort,
    private val updateInvestigatorStudyRelationOutPort: UpdateInvestigatorStudyRelationOutPort
) : UpdateInvestigatorUseCase {
    @Role(actions = [ACTION_WRITE], tenants = [PUBLIC_TENANT])
    override suspend fun updateInvestigator(
        @Resources investigatorId: String,
        email: String,
        command: UpdateInvestigatorCommand
    ) {
        val investigator = command.toDomain(investigatorId, email)
        updateInvestigatorOutPort.updateInvestigator(investigator)
    }

    @Role(actions = [ACTION_WRITE])
    override suspend fun updateInvestigatorRole(
        investigatorId: String,
        @Tenants studyId: String,
        @Resources role: String
    ): String {
        val updatedRole = updateRoleOutPort.updateRole(investigatorId, studyId, role)

        // TODO: Do we need to maintain the relation information after the role assigned?
        val investigator = getInvestigatorOutPort.getInvestigator(investigatorId)
        updateInvestigatorStudyRelationOutPort.updateRelation(investigator.email.value, studyId, role)

        return updatedRole
    }
}
