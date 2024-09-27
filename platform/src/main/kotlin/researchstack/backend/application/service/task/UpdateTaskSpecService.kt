package researchstack.backend.application.service.task

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_TASK_SPEC
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.task.UpdateTaskSpecCommand
import researchstack.backend.application.port.incoming.task.UpdateTaskSpecUseCase
import researchstack.backend.application.port.outgoing.task.GetTaskSpecOutPort
import researchstack.backend.application.port.outgoing.task.UpdateTaskSpecOutPort
import researchstack.backend.domain.task.TaskSpec

@Service
class UpdateTaskSpecService(
    private val getTaskSpecOutPort: GetTaskSpecOutPort,
    private val updateTaskSpecOutPort: UpdateTaskSpecOutPort
) : UpdateTaskSpecUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_TASK_SPEC])
    override suspend fun updateTaskSpec(
        command: UpdateTaskSpecCommand,
        taskId: String,
        @Tenants studyId: String
    ) {
        val existingTaskSpec = getTaskSpecOutPort.getTaskSpec(taskId)
        val taskSpec = command.toDomain(taskId, studyId)
        updateTaskSpecOutPort.updateTaskSpec(
            TaskSpec(
                id = taskSpec.id,
                studyId = taskSpec.studyId,
                title = taskSpec.title,
                description = taskSpec.description,
                status = taskSpec.status,
                schedule = taskSpec.schedule,
                createdAt = taskSpec.createdAt ?: existingTaskSpec.createdAt,
                publishedAt = taskSpec.publishedAt ?: existingTaskSpec.publishedAt,
                startTime = taskSpec.startTime,
                endTime = taskSpec.endTime,
                validMin = taskSpec.validMin,
                duration = taskSpec.duration,
                iconUrl = taskSpec.iconUrl ?: existingTaskSpec.iconUrl,
                inClinic = taskSpec.inClinic,
                taskType = taskSpec.taskType,
                task = taskSpec.task
            )
        )
    }
}
