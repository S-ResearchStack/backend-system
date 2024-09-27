package researchstack.backend.adapter.incoming.rest.inlabvisit

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
import researchstack.backend.application.port.incoming.common.PaginationCommand
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitCommand
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitUseCase
import researchstack.backend.application.port.incoming.inlabvisit.DeleteInLabVisitUseCase
import researchstack.backend.application.port.incoming.inlabvisit.GetInLabVisitUseCase
import researchstack.backend.application.port.incoming.inlabvisit.UpdateInLabVisitCommand
import researchstack.backend.application.port.incoming.inlabvisit.UpdateInLabVisitUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.util.validateContext

@Component
@Decorator(TenantHandlerHttpFunction::class)
class InLabVisitRestController(
    private val createInLabVisitUseCase: CreateInLabVisitUseCase,
    private val getInLabVisitUseCase: GetInLabVisitUseCase,
    private val updateInLabVisitUseCase: UpdateInLabVisitUseCase,
    private val deleteInLabVisitUseCase: DeleteInLabVisitUseCase
) {
    @Post("/studies/{studyId}/in-lab-visits")
    suspend fun createInLabVisit(
        @Param studyId: String,
        @RequestObject command: CreateInLabVisitCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(command.picId, ExceptionMessage.EMPTY_IN_LAB_VISIT_PIC_ID)

        val createResponse = createInLabVisitUseCase.createInLabVisit(
            studyId = studyId,
            investigatorId = userId,
            command = command
        )
        return HttpResponse.of(JsonHandler.toJson(createResponse))
    }

    @Get("/studies/{studyId}/in-lab-visits")
    suspend fun getInLabVisitList(
        @Param studyId: String,
        @RequestObject paginationCommand: PaginationCommand? = null
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)

        val inLabVisitListResponse = getInLabVisitUseCase.getInLabVisitList(studyId)
        return HttpResponse.of(JsonHandler.toJson(inLabVisitListResponse))
    }

    @Patch("/studies/{studyId}/in-lab-visits/{inLabVisitId}")
    suspend fun updateInLabVisit(
        @Param studyId: String,
        @Param inLabVisitId: String,
        @RequestObject command: UpdateInLabVisitCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(inLabVisitId, ExceptionMessage.EMPTY_IN_LAB_VISIT_ID)

        updateInLabVisitUseCase.updateInLabVisit(studyId, inLabVisitId, command)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/studies/{studyId}/in-lab-visits/{inLabVisitId}")
    suspend fun deleteInLabVisit(
        @Param studyId: String,
        @Param inLabVisitId: String
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(inLabVisitId, ExceptionMessage.EMPTY_IN_LAB_VISIT_ID)

        deleteInLabVisitUseCase.deleteInLabVisit(studyId, inLabVisitId)
        return HttpResponse.of(HttpStatus.OK)
    }
}
