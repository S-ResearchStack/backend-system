package researchstack.backend.adapter.incoming.rest.study

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
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementCommand
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.DeleteParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.GetParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.UpdateParticipationRequirementCommand
import researchstack.backend.application.port.incoming.study.UpdateParticipationRequirementUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.util.validateContext

@Component
class ParticipationRequirementRestController(
    private val createUseCase: CreateParticipationRequirementUseCase,
    private val getUseCase: GetParticipationRequirementUseCase,
    private val updateUseCase: UpdateParticipationRequirementUseCase,
    private val deleteUseCase: DeleteParticipationRequirementUseCase
) {
    @Post("/studies/{studyId}/requirements")
    suspend fun createParticipationRequirement(
        @Param("studyId") studyId: String,
        @RequestObject command: CreateParticipationRequirementCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        createUseCase.create(command, studyId)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Get("/studies/{studyId}/requirements")
    suspend fun getParticipationRequirement(@Param("studyId") studyId: String): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val participationRequirement = getUseCase.getParticipationRequirementForHttp(studyId)
        return HttpResponse.of(JsonHandler.toJson(participationRequirement))
    }

    @Patch("/studies/{studyId}/requirements")
    suspend fun updateParticipationRequirement(
        @Param("studyId") studyId: String,
        @RequestObject command: UpdateParticipationRequirementCommand
    ): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        updateUseCase.update(studyId, command)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/studies/{studyId}/requirements")
    suspend fun deleteParticipationRequirement(@Param("studyId") studyId: String): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        deleteUseCase.delete(studyId)
        return HttpResponse.of(HttpStatus.OK)
    }
}
