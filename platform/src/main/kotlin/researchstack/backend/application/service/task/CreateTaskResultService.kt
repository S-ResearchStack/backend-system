package researchstack.backend.application.service.task

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_TASK_RESULT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.task.CreateTaskResultUseCase
import researchstack.backend.application.port.incoming.task.TaskResultCommand
import researchstack.backend.application.port.outgoing.task.CreateTaskResultOutPort
import researchstack.backend.application.service.mapper.toDomain
import researchstack.backend.domain.subject.Subject.SubjectId

@Service
class CreateTaskResultService(
    private val createTaskResultOutPort: CreateTaskResultOutPort
) : CreateTaskResultUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_TASK_RESULT])
    override suspend fun createTaskResult(
        subjectId: SubjectId,
        @Tenants studyId: String,
        taskResultCommand: TaskResultCommand
    ) {
        val taskResult = taskResultCommand.toDomain()
        createTaskResultOutPort.createTaskResult(subjectId.value, taskResult)
    }
}
