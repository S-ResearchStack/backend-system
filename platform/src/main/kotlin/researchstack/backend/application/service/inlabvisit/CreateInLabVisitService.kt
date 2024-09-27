package researchstack.backend.application.service.inlabvisit

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_IN_LAB_VISIT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitCommand
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitResponse
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitUseCase
import researchstack.backend.application.port.outgoing.inlabvisit.CreateInLabVisitOutPort
import researchstack.backend.application.service.mapper.toDomain

@Service
class CreateInLabVisitService(
    private val createInLabVisitOutPort: CreateInLabVisitOutPort
) : CreateInLabVisitUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_IN_LAB_VISIT])
    override suspend fun createInLabVisit(
        @Tenants
        studyId: String,
        investigatorId: String,
        command: CreateInLabVisitCommand
    ): CreateInLabVisitResponse {
        val inLabVisit = createInLabVisitOutPort.createInLabVisit(command.toDomain(investigatorId))
        return CreateInLabVisitResponse(inLabVisit.id)
    }
}
