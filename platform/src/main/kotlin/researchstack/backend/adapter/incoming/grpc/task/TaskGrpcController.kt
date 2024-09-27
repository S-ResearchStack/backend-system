package researchstack.backend.adapter.incoming.grpc.task

import com.google.protobuf.Empty
import com.linecorp.armeria.server.ServiceRequestContext
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.mapper.task.toCommand
import researchstack.backend.adapter.incoming.mapper.task.toGrpc
import researchstack.backend.adapter.incoming.mapper.toLocalDateTime
import researchstack.backend.application.port.incoming.task.CreateTaskResultUseCase
import researchstack.backend.application.port.incoming.task.GetTaskSpecUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.grpc.AllNewTaskRequest
import researchstack.backend.grpc.TaskResultUploadRequest
import researchstack.backend.grpc.TaskServiceGrpcKt
import researchstack.backend.grpc.TaskSpecRequest
import researchstack.backend.grpc.TaskSpecResponse
import researchstack.backend.util.validateContext

@Component
class TaskGrpcController(
    private val getTaskSpecUseCase: GetTaskSpecUseCase,
    private val createTaskResultUseCase: CreateTaskResultUseCase
) : TaskServiceGrpcKt.TaskServiceCoroutineImplBase() {

    override suspend fun getTaskSpecs(request: TaskSpecRequest): TaskSpecResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        return TaskSpecResponse.newBuilder().addAllTaskSpecs(
            getTaskSpecUseCase.getTaskSpecs(request.studyId).map { it.toGrpc() }
        ).build()
    }

    override suspend fun getAllTaskSpecs(request: Empty): TaskSpecResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        return TaskSpecResponse.newBuilder().addAllTaskSpecs(
            getTaskSpecUseCase.getAllTaskSpecs(
                Subject.SubjectId.from(userId)
            ).map { it.toGrpc() }
        ).build()
    }

    override suspend fun getAllNewTaskSpecs(request: AllNewTaskRequest): TaskSpecResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        return TaskSpecResponse.newBuilder().addAllTaskSpecs(
            getTaskSpecUseCase.getAllNewTaskSpecs(
                Subject.SubjectId.from(userId),
                request.lastSyncTime.toLocalDateTime()
            ).map { it.toGrpc() }
        ).build()
    }

    override suspend fun uploadTaskResult(request: TaskResultUploadRequest): Empty {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        createTaskResultUseCase.createTaskResult(
            Subject.SubjectId.from(userId),
            request.taskResult.studyId,
            request.taskResult.toCommand()
        )
        return Empty.newBuilder().build()
    }
}
