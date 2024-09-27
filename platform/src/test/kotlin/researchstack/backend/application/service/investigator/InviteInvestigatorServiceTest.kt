package researchstack.backend.application.service.investigator

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorCommand
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.AddInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.application.service.common.EmailService
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class InviteInvestigatorServiceTest {
    private val relationOutPort = mockk<AddInvestigatorStudyRelationOutPort>()
    private val getInvestigatorOutPort = mockk<GetInvestigatorOutPort>()
    private val emailService = mockk<EmailService>()
    private val addRoleOutPort = mockk<AddRoleOutPort>()

    private val inviteInvestigatorService = InviteInvestigatorUseService(
        relationOutPort = relationOutPort,
        getInvestigatorOutPort = getInvestigatorOutPort,
        emailService = emailService,
        addRoleOutPort = addRoleOutPort
    )

    @Tag(POSITIVE_TEST)
    @Test
    fun `inviteInvestigator should return true`() = runTest {
        val inviterId = "test-inviterId"
        val inviteeId = "test-inviteeId"

        val email = "test@test.com"
        val studyId = "test-studyId"
        val role = "test-role"
        val command = InviteInvestigatorCommand(
            email = email,
            studyId = studyId,
            role = role
        )

        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val invitedInvestigator = Investigator(
            id = inviteeId,
            firstName = firstName,
            lastName = lastName,
            company = company,
            team = team,
            email = Email(email),
            officePhoneNumber = officePhoneNumber,
            mobilePhoneNumber = mobilePhoneNumber
        )

        coJustRun {
            relationOutPort.addRelation(any())
        }

        coEvery {
            getInvestigatorOutPort.getInvestigatorByEmail(email)
        } returns invitedInvestigator

        coEvery {
            addRoleOutPort.addRole(inviteeId, studyId, role)
        } returns true

        assertTrue {
            inviteInvestigatorService.inviteInvestigator(inviterId, command.studyId, command.role, command)
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `inviteInvestigator should return true although the investigator hasn't registered yet`() = runTest {
        val inviterId = "test-inviterId"

        val email = "test@test.com"
        val studyId = "test-studyId"
        val role = "test-role"
        val command = InviteInvestigatorCommand(
            email = email,
            studyId = studyId,
            role = role
        )

        coJustRun {
            relationOutPort.addRelation(any())
        }

        coEvery {
            getInvestigatorOutPort.getInvestigatorByEmail(email)
        } throws NoSuchElementException()

        assertTrue {
            inviteInvestigatorService.inviteInvestigator(inviterId, command.studyId, command.role, command)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `inviteInvestigator should throw IllegalArgumentException if email is empty`(email: String) = runTest {
        val inviterId = "test-inviterId"
        val studyId = "test-studyId"
        val role = "test-role"
        val exception = assertThrows<IllegalArgumentException> {
            InviteInvestigatorCommand(email, studyId, role).let {
                inviteInvestigatorService.inviteInvestigator(
                    inviterId,
                    it.studyId,
                    it.role,
                    it
                )
            }
        }
        assertEquals(ExceptionMessage.EMPTY_EMAIL, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `inviteInvestigator should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        val inviterId = "test-inviterId"
        val email = "test@test.com"
        val role = "test-role"
        val exception = assertThrows<IllegalArgumentException> {
            InviteInvestigatorCommand(email, studyId, role).let {
                inviteInvestigatorService.inviteInvestigator(
                    inviterId,
                    it.studyId,
                    it.role,
                    it
                )
            }
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `inviteInvestigator should throw IllegalArgumentException if role is empty`(role: String) = runTest {
        val inviterId = "test-inviterId"
        val email = "test@test.com"
        val studyId = "test-studyId"
        val exception = assertThrows<IllegalArgumentException> {
            InviteInvestigatorCommand(email, studyId, role).let {
                inviteInvestigatorService.inviteInvestigator(
                    inviterId,
                    it.studyId,
                    it.role,
                    it
                )
            }
        }
        assertEquals(ExceptionMessage.EMPTY_ROLE, exception.message)
    }
}
