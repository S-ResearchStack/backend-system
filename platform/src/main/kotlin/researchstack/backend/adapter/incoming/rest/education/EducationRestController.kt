package researchstack.backend.adapter.incoming.rest.education

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Decorator
import com.linecorp.armeria.server.annotation.Delete
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Patch
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.decorator.TenantHandlerHttpFunction
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.education.CreateEducationalContentCommand
import researchstack.backend.application.port.incoming.education.CreateEducationalContentUseCase
import researchstack.backend.application.port.incoming.education.DeleteEducationalContentUseCase
import researchstack.backend.application.port.incoming.education.GetEducationalContentUseCase
import researchstack.backend.application.port.incoming.education.UpdateEducationalContentCommand
import researchstack.backend.application.port.incoming.education.UpdateEducationalContentUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.enums.EducationalContentStatus
import researchstack.backend.util.validateContext

@Component
@Decorator(TenantHandlerHttpFunction::class)
class EducationRestController(
    private val createEducationalContentUseCase: CreateEducationalContentUseCase,
    private val getEducationalContentUseCase: GetEducationalContentUseCase,
    private val updateEducationalContentUseCase: UpdateEducationalContentUseCase,
    private val deleteEducationalContentUseCase: DeleteEducationalContentUseCase
) {
    @Post("/studies/{studyId}/educational-contents")
    suspend fun createEducationalContent(
        @Param studyId: String,
        @RequestObject command: CreateEducationalContentCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(command.title, ExceptionMessage.EMPTY_EDUCATION_TITLE)
        validateContext(command.category, ExceptionMessage.EMPTY_EDUCATION_CATEGORY)
        val createResponse = createEducationalContentUseCase.createEducationalContent(
            studyId = studyId,
            investigatorId = userId,
            command = command
        )
        return HttpResponse.of(JsonHandler.toJson(createResponse))
    }

    @Get("/studies/{studyId}/educational-contents/{contentId}")
    suspend fun getEducationalContent(@Param studyId: String, @Param contentId: String): HttpResponse {
        validateContext(ServiceRequestContext.current().getUserId(), ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(contentId, ExceptionMessage.EMPTY_EDUCATIONAL_CONTENT_ID)
        val educationalContent = getEducationalContentUseCase.getEducationalContent(studyId, contentId)
        return HttpResponse.of(JsonHandler.toJson(educationalContent))
    }

    @Get("/studies/{studyId}/educational-contents")
    suspend fun getEducationalContentList(
        @Param studyId: String,
        @Param status: EducationalContentStatus? = null
    ): HttpResponse {
        validateContext(ServiceRequestContext.current().getUserId(), ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val educationalContentList = getEducationalContentUseCase.getEducationalContentList(studyId, status)
        return HttpResponse.of(JsonHandler.toJson(educationalContentList))
    }

    @Patch("/studies/{studyId}/educational-contents/{contentId}")
    suspend fun updateEducationalContent(
        @Param studyId: String,
        @Param contentId: String,
        @RequestObject command: UpdateEducationalContentCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(contentId, ExceptionMessage.EMPTY_EDUCATIONAL_CONTENT_ID)

        updateEducationalContentUseCase.updateEducationalContent(studyId, contentId, command)

        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/studies/{studyId}/educational-contents/{contentId}")
    suspend fun deleteEducationalContent(
        @Param studyId: String,
        @Param contentId: String
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(contentId, ExceptionMessage.EMPTY_EDUCATIONAL_CONTENT_ID)

        deleteEducationalContentUseCase.deleteEducationalContent(studyId, contentId)

        return HttpResponse.of(HttpStatus.OK)
    }
}
