package researchstack.backend.application.service.task

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_TASK_SPEC
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.task.DeleteTaskSpecUseCase
import researchstack.backend.application.port.outgoing.task.DeleteTaskSpecOutPort

@Service
class DeleteTaskSpecService(
    private val deleteTaskSpecOutPort: DeleteTaskSpecOutPort
) : DeleteTaskSpecUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_TASK_SPEC])
    override suspend fun deleteTaskSpec(taskId: String, @Tenants studyId: String) {
        deleteTaskSpecOutPort.deleteTaskSpec(taskId)
    }
}
