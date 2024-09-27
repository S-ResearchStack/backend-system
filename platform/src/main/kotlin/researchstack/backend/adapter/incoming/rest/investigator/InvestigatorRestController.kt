package researchstack.backend.adapter.incoming.rest.investigator

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
import researchstack.backend.application.port.incoming.investigator.DeleteInvestigatorRoleCommand
import researchstack.backend.application.port.incoming.investigator.DeleteInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.GetInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorRoleCommand
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorUseCase
import researchstack.backend.config.getEmail
import researchstack.backend.config.getUserId
import researchstack.backend.util.validateContext

@Component
class InvestigatorRestController(
    val registerInvestigatorUseCase: RegisterInvestigatorUseCase,
    val getInvestigatorUseCase: GetInvestigatorUseCase,
    val updateInvestigatorUseCase: UpdateInvestigatorUseCase,
    val inviteInvestigatorUseCase: InviteInvestigatorUseCase,
    val deleteInvestigatorUseCase: DeleteInvestigatorUseCase
) {

    @Post("/investigators")
    suspend fun registerInvestigator(@RequestObject command: RegisterInvestigatorCommand): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        val email = ServiceRequestContext.current().getEmail()
        validateContext(investigatorId, ExceptionMessage.EMPTY_INVESTIGATOR_ID)
        validateContext(email, ExceptionMessage.EMPTY_EMAIL)
        registerInvestigatorUseCase.registerInvestigator(investigatorId, email, command)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Get("/investigators/me")
    suspend fun getInvestigator(): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_INVESTIGATOR_ID)
        val investigator = getInvestigatorUseCase.getInvestigator(investigatorId)
        return HttpResponse.of(JsonHandler.toJson(investigator))
    }

    @Patch("/investigators")
    suspend fun updateInvestigator(
        @RequestObject command: UpdateInvestigatorCommand
    ): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        val email = ServiceRequestContext.current().getEmail()
        validateContext(investigatorId, ExceptionMessage.EMPTY_INVESTIGATOR_ID)
        validateContext(email, ExceptionMessage.EMPTY_EMAIL)
        updateInvestigatorUseCase.updateInvestigator(investigatorId, email, command)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Get("/investigators")
    suspend fun getInvestigators(@Param("studyId") studyId: String): HttpResponse {
        val investigatorId = ServiceRequestContext.current().getUserId()
        validateContext(investigatorId, ExceptionMessage.EMPTY_INVESTIGATOR_ID)
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val investigators = getInvestigatorUseCase.getInvestigators(studyId)
        return HttpResponse.of(JsonHandler.toJson(investigators))
    }

    @Post("/investigators/invite")
    suspend fun inviteInvestigator(@RequestObject command: InviteInvestigatorCommand): HttpResponse {
        val inviterId = ServiceRequestContext.current().getUserId()
        validateContext(inviterId, ExceptionMessage.EMPTY_USER_ID)
        inviteInvestigatorUseCase.inviteInvestigator(inviterId, command.studyId, command.role, command)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Patch("/investigators/{investigatorId}/roles")
    suspend fun updateInvestigatorRole(
        @Param("investigatorId") investigatorId: String,
        @RequestObject command: UpdateInvestigatorRoleCommand
    ): HttpResponse {
        validateContext(investigatorId, ExceptionMessage.EMPTY_INVESTIGATOR_ID)
        updateInvestigatorUseCase.updateInvestigatorRole(investigatorId, command.studyId, command.role)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/investigators/{investigatorId}/roles")
    suspend fun deleteInvestigatorRole(
        @Param("investigatorId") investigatorId: String,
        @RequestObject command: DeleteInvestigatorRoleCommand
    ): HttpResponse {
        val removerId = ServiceRequestContext.current().getUserId()
        validateContext(removerId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(investigatorId, ExceptionMessage.EMPTY_INVESTIGATOR_ID)
        deleteInvestigatorUseCase.deleteInvestigatorRole(investigatorId, command.studyId)
        return HttpResponse.of(HttpStatus.OK)
    }
}
