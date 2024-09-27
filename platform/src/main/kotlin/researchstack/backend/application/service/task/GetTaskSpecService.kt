package researchstack.backend.application.service.task

import org.springframework.stereotype.Service
import researchstack.backend.adapter.incoming.mapper.task.toResponse
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_TASK_SPEC
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.incoming.task.GetTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.TaskSpecResponse
import researchstack.backend.application.port.outgoing.task.GetTaskSpecOutPort
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.TaskStatus
import java.time.LocalDateTime

@Service
class GetTaskSpecService(
    private val getTaskSpecOutPort: GetTaskSpecOutPort
) : GetTaskSpecUseCase {
    @Role(actions = [ACTION_READ], resources = [RESOURCE_TASK_SPEC])
    override suspend fun getTaskSpec(
        @Tenants studyId: String,
        taskId: String
    ): TaskSpecResponse {
        val taskSpec = getTaskSpecOutPort.getTaskSpec(taskId)
        if (taskSpec.studyId != studyId) {
            throw NotFoundException("no task($taskId) on study($studyId)")
        }
        return taskSpec.toResponse()
    }

    @Role(actions = [ACTION_READ], resources = [RESOURCE_TASK_SPEC])
    override suspend fun getTaskSpecs(@Tenants studyId: String, status: TaskStatus?): List<TaskSpecResponse> {
        return if (status == null) {
            getTaskSpecsInternal(studyId)
        } else {
            getTaskSpecOutPort
                .getTaskSpecs(studyId, status)
                .map { it.toResponse() }
        }
    }

    override suspend fun getAllTaskSpecs(subjectId: Subject.SubjectId): List<TaskSpecResponse> {
        val taskSpecs = getTaskSpecOutPort.getAllTaskSpecs(subjectId)
        return taskSpecs.map { it.toResponse() }
    }

    override suspend fun getAllNewTaskSpecs(
        subjectId: Subject.SubjectId,
        lastSyncTime: LocalDateTime
    ): List<TaskSpecResponse> {
        val taskSpecs = getTaskSpecOutPort.getAllNewTaskSpecs(subjectId, lastSyncTime)
        return taskSpecs.map { it.toResponse() }
    }

    suspend fun getTaskSpecsInternal(studyId: String): List<TaskSpecResponse> {
        val taskSpecs = getTaskSpecOutPort.getTaskSpecs(studyId)
        return taskSpecs.map { it.toResponse() }
    }
}
