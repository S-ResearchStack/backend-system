package researchstack.backend.application.service.investigator

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.role.Resources
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.common.SendInvitationEmailCommand
import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorUseCase
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.AddInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.application.service.common.EmailService
import researchstack.backend.application.service.mapper.toDomain

@Service
class InviteInvestigatorUseService(
    private val relationOutPort: AddInvestigatorStudyRelationOutPort,
    private val getInvestigatorOutPort: GetInvestigatorOutPort,
    private val emailService: EmailService,
    private val addRoleOutPort: AddRoleOutPort
) : InviteInvestigatorUseCase {
    @Role(actions = [ACTION_WRITE])
    override suspend fun inviteInvestigator(
        inviterInvestigatorId: String,
        @Tenants studyId: String,
        @Resources role: String,
        command: InviteInvestigatorCommand
    ): Boolean {
        relationOutPort.addRelation(command.toDomain())

        var result = true
        try {
            val invitedInvestigator = getInvestigatorOutPort.getInvestigatorByEmail(command.email)
            result = addRoleOutPort.addRole(invitedInvestigator.id, command.studyId, command.role)
        } catch (_: NoSuchElementException) {
            // TODO: This is not an error. Should we do something here?
        }

        // TODO: check email service
        // val inviter = getAnalystOutPort.getAnalyst(inviterAnalystId)
        // sendEmail(
        //    sender = "${inviter.firstName} ${inviter.lastName}",
        //    email = command.email,
        //    studyName = command.studyId,
        //    role = command.role
        // )

        // TODO: combine with email result
        return result
    }

    private suspend fun sendEmail(sender: String, email: String, studyName: String, role: String) {
        val sendEmailCommand = SendInvitationEmailCommand(
            email = email,
            sender = sender,
            studyName = studyName,
            role = role
        )
        emailService.sendInvitationEmail(sendEmailCommand)
    }
}
