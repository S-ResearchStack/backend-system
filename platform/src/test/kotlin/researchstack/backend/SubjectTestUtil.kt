package researchstack.backend

import researchstack.backend.application.port.incoming.subject.RegisterSubjectCommand
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileCommand
import researchstack.backend.domain.subject.Subject
import kotlin.test.assertEquals

class SubjectTestUtil {

    companion object {
        const val subjectId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
        private const val firstName = "Kevin"
        private const val lastName = "Kim"
        private const val birthdayYear = 2000
        private const val birthdayMonth = 11
        private const val birthdayDay = 1
        private const val email = "kevin.kim@example.com"
        private const val phoneNumber = "010-1234-0000"
        private const val address = "Seoul"
        private const val officePhoneNumber = "02-0000-0000"
        private const val company = "Samsung"
        private const val team = "Data Intelligence"

        fun createRegisterSubjectCommand(subjectId: String?): RegisterSubjectCommand {
            return RegisterSubjectCommand(
                subjectId = subjectId,
                firstName = firstName,
                lastName = lastName,
                birthdayYear = birthdayYear,
                birthdayMonth = birthdayMonth,
                birthdayDay = birthdayDay,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                officePhoneNumber = officePhoneNumber,
                company = company,
                team = team
            )
        }

        fun createUpdateSubjectProfileCommand(): UpdateSubjectProfileCommand {
            return UpdateSubjectProfileCommand(
                firstName = firstName,
                lastName = lastName,
                birthdayYear = birthdayYear,
                birthdayMonth = birthdayMonth,
                birthdayDay = birthdayDay,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                officePhoneNumber = officePhoneNumber,
                company = company,
                team = team
            )
        }

        fun compareSubjectProfile(expected: Subject, actual: Subject) {
            assertEquals(expected.subjectId, actual.subjectId)
            assertEquals(expected.firstName, actual.firstName)
            assertEquals(expected.lastName, actual.lastName)
            assertEquals(expected.birthdayYear, actual.birthdayYear)
            assertEquals(expected.birthdayMonth, actual.birthdayMonth)
            assertEquals(expected.birthdayDay, actual.birthdayDay)
            assertEquals(expected.email, actual.email)
            assertEquals(expected.phoneNumber, actual.phoneNumber)
            assertEquals(expected.address, actual.address)
            assertEquals(expected.officePhoneNumber, actual.officePhoneNumber)
            assertEquals(expected.company, actual.company)
            assertEquals(expected.team, actual.team)
        }

        val subjectDomain = Subject(
            Subject.SubjectId.from(subjectId),
            firstName,
            lastName,
            birthdayYear,
            birthdayMonth,
            birthdayDay,
            email,
            phoneNumber,
            address,
            officePhoneNumber,
            company,
            team
        )
    }
}
