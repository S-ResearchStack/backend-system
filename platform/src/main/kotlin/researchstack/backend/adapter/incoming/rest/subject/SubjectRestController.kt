package researchstack.backend.adapter.incoming.rest.subject

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Delete
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Patch
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.subject.DeregisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.GetSubjectUseCase
import researchstack.backend.application.port.incoming.subject.RegisterSubjectCommand
import researchstack.backend.application.port.incoming.subject.RegisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileCommand
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.util.validateContext

@Component
class SubjectRestController(
    private val registerSubjectUseCase: RegisterSubjectUseCase,
    private val deregisterSubjectUseCase: DeregisterSubjectUseCase,
    private val getSubjectUseCase: GetSubjectUseCase,
    private val updateSubjectProfileUseCase: UpdateSubjectProfileUseCase
) {
    private val logger = LoggerFactory.getLogger(SubjectRestController::class.java)

    @Post("/subjects")
    suspend fun registerSubject(
        @RequestObject command: RegisterSubjectCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        logger.info("User ID: $userId")
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        command.subjectId = userId
        registerSubjectUseCase.registerSubject(command)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/subjects")
    suspend fun deregisterSubject(): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        logger.info("User ID: $userId")
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        deregisterSubjectUseCase.deregisterSubject(Subject.SubjectId.from(userId))
        return HttpResponse.of(HttpStatus.OK)
    }

    @Get("/subjects")
    suspend fun getUserProfile(): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val subject = getSubjectUseCase.getSubjectProfile(Subject.SubjectId.from(userId))
        return HttpResponse.of(JsonHandler.toJson(subject))
    }

    @Get("/studies/{studyId}/subject/statuses")
    suspend fun getSubjectStatus(@Param("studyId") studyId: String): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val userStatus = getSubjectUseCase.getSubjectStatus(Subject.SubjectId.from(userId), studyId)
        return HttpResponse.of(JsonHandler.toJson(userStatus))
    }

    @Patch("/subjects")
    suspend fun updateSubjectProfile(
        @RequestObject command: UpdateSubjectProfileCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        updateSubjectProfileUseCase.updateSubjectProfile(Subject.SubjectId.from(userId), command)
        return HttpResponse.of(HttpStatus.OK)
    }
}
