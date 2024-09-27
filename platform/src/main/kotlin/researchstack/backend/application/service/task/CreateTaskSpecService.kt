package researchstack.backend.application.service.task

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_TASK_SPEC
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.task.CreateTaskSpecCommand
import researchstack.backend.application.port.incoming.task.CreateTaskSpecUseCase
import researchstack.backend.application.port.outgoing.task.CreateTaskSpecOutPort

@Service
class CreateTaskSpecService(
    private val createTaskSpecOutPort: CreateTaskSpecOutPort
) : CreateTaskSpecUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_TASK_SPEC])
    override suspend fun createTaskSpec(command: CreateTaskSpecCommand, @Tenants studyId: String) {
        createTaskSpecOutPort.createTaskSpec(command.toDomain(studyId))
    }
}
