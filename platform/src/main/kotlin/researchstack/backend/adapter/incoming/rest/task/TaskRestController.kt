package researchstack.backend.adapter.incoming.rest.task

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Delete
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Patch
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.mapper.task.toTaskResultCommand
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.task.CreateTaskResultUseCase
import researchstack.backend.application.port.incoming.task.CreateTaskSpecCommand
import researchstack.backend.application.port.incoming.task.CreateTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.DeleteTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.GetTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.TaskResultRestCommand
import researchstack.backend.application.port.incoming.task.UpdateTaskSpecCommand
import researchstack.backend.application.port.incoming.task.UpdateTaskSpecUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.TaskStatus
import researchstack.backend.util.validateContext
import java.sql.Timestamp

@Component
class TaskRestController(
    private val createUseCase: CreateTaskSpecUseCase,
    private val getUseCase: GetTaskSpecUseCase,
    private val updateUseCase: UpdateTaskSpecUseCase,
    private val deleteUseCase: DeleteTaskSpecUseCase,
    private val createTaskResultUseCase: CreateTaskResultUseCase
) {
    @Post("/studies/{studyId}/tasks")
    suspend fun createTaskSpec(
        @Param("studyId") studyId: String,
        @RequestObject command: CreateTaskSpecCommand
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        createUseCase.createTaskSpec(command, studyId)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Get("/studies/{studyId}/tasks/{taskId}")
    suspend fun getTaskSpec(
        @Param("studyId") studyId: String,
        @Param("taskId") taskId: String
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(taskId, ExceptionMessage.EMPTY_TASK_ID)
        val taskSpec = getUseCase.getTaskSpec(studyId, taskId)
        return HttpResponse.of(JsonHandler.toJson(taskSpec))
    }

    @Get("/studies/{studyId}/tasks")
    suspend fun getTaskSpecs(
        @Param("studyId") studyId: String,
        @Param("status") status: TaskStatus?
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val taskSpecs = getUseCase.getTaskSpecs(studyId, status)
        return HttpResponse.of(JsonHandler.toJson(taskSpecs))
    }

    @Get("/tasks")
    suspend fun getAllTaskSpecs(): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val taskSpecs = getUseCase.getAllTaskSpecs(Subject.SubjectId.from(userId))
        return HttpResponse.of(JsonHandler.toJson(taskSpecs))
    }

    @Get("/studies/{studyId}/tasks/me")
    suspend fun getAllNewTaskSpecs(
        @Param("studyId") studyId: String,
        @Param("lastSyncTime") lastSyncTime: Timestamp
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val taskSpecs = getUseCase.getAllNewTaskSpecs(
            Subject.SubjectId.from(userId),
            lastSyncTime.toLocalDateTime()
        )
        return HttpResponse.of(JsonHandler.toJson(taskSpecs))
    }

    @Patch("/studies/{studyId}/tasks/{taskId}")
    suspend fun updateTaskSpec(
        @Param("studyId") studyId: String,
        @Param("taskId") taskId: String,
        @RequestObject command: UpdateTaskSpecCommand
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(taskId, ExceptionMessage.EMPTY_TASK_ID)
        updateUseCase.updateTaskSpec(command, taskId, studyId)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/studies/{studyId}/tasks/{taskId}")
    suspend fun deleteTaskSpec(
        @Param("studyId") studyId: String,
        @Param("taskId") taskId: String
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(taskId, ExceptionMessage.EMPTY_TASK_ID)
        deleteUseCase.deleteTaskSpec(taskId, studyId)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Post("/studies/{studyId}/tasks/{taskId}/results")
    suspend fun uploadTaskResult(
        @Param("studyId") studyId: String,
        @Param("taskId") taskId: String,
        @RequestObject command: TaskResultRestCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(taskId, ExceptionMessage.EMPTY_TASK_ID)
        createTaskResultUseCase.createTaskResult(
            Subject.SubjectId.from(userId),
            studyId,
            command.toTaskResultCommand(studyId, taskId)
        )
        return HttpResponse.of(HttpStatus.OK)
    }
}
