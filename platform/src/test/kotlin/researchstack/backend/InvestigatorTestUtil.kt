package researchstack.backend

import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorRoleCommand
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator

class InvestigatorTestUtil {
    companion object {
        const val email = "test@example.com"
        const val investigatorId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
        const val studyId = "test-study-id"
        private const val firstName = "Culater"
        private const val lastName = "Kwak"
        private const val company = "Samsung"
        private const val team = "Data Intelligence"
        private const val officePhoneNumber = "02-0000-0000"
        private const val phoneNumber = "010-1234-0000"
        private const val invitedEmail = "test2@samsung.com"
        const val invitedRole = "test-role"

        fun createRegisterInvestigatorCommand(): RegisterInvestigatorCommand {
            return RegisterInvestigatorCommand(
                firstName,
                lastName,
                company,
                team,
                officePhoneNumber,
                phoneNumber,
                null
            )
        }

        fun createUpdateInvestigatorCommand() = UpdateInvestigatorCommand(
            firstName,
            lastName,
            company,
            team,
            officePhoneNumber,
            phoneNumber,
            null
        )

        fun createInviteInvestigatorCommand() = InviteInvestigatorCommand(
            invitedEmail,
            studyId,
            invitedRole
        )

        fun createUpdateInvestigatorRoleCommand() = UpdateInvestigatorRoleCommand(
            studyId,
            invitedRole
        )

        fun createInvestigator(): Investigator {
            return Investigator(
                investigatorId,
                Email(email),
                firstName,
                lastName,
                company,
                team,
                officePhoneNumber,
                phoneNumber,
                null,
                null
            )
        }
    }
}
