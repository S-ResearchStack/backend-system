package researchstack.backend.application.service.dashboard

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_DASHBOARD
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardCommand
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardUseCase
import researchstack.backend.application.port.outgoing.dashboard.CreateDashboardOutPort
import researchstack.backend.application.service.mapper.toDomain

@Service
class CreateDashboardService(
    private val createDashboardOutPort: CreateDashboardOutPort
) : CreateDashboardUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_DASHBOARD])
    override suspend fun createDashboard(@Tenants studyId: String, command: CreateDashboardCommand): String {
        return createDashboardOutPort.createDashboard(command.toDomain(studyId))
    }
}
