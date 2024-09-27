package researchstack.backend.application.service.investigator

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.InvestigatorTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.casbin.UpdateRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.application.port.outgoing.investigator.UpdateInvestigatorOutPort
import researchstack.backend.application.port.outgoing.investigator.UpdateInvestigatorStudyRelationOutPort
import researchstack.backend.application.service.mapper.toDomain
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator
import researchstack.backend.domain.investigator.InvestigatorStudyRelation
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class UpdateInvestigatorServiceTest {
    private val updateInvestigatorOutPort = mockk<UpdateInvestigatorOutPort>()
    private val updateRoleOutPort = mockk<UpdateRoleOutPort>()
    private val getInvestigatorOutPort = mockk<GetInvestigatorOutPort>()
    private val updateRelationOutPort = mockk<UpdateInvestigatorStudyRelationOutPort>()

    private val updateInvestigatorService = UpdateInvestigatorService(
        updateInvestigatorOutPort,
        updateRoleOutPort,
        getInvestigatorOutPort,
        updateRelationOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInvestigator should work properly`() = runTest {
        val investigatorId = "test-investigatorId"
        val email = "test@test.com"
        val command = InvestigatorTestUtil.createUpdateInvestigatorCommand()
        val investigator = command.toDomain(investigatorId, email)

        coEvery {
            updateInvestigatorOutPort.updateInvestigator(investigator)
        } returns Unit

        assertDoesNotThrow {
            updateInvestigatorService.updateInvestigator(investigatorId, email, command)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInvestigatorRole should return the role that same as requested`() = runTest {
        val investigatorId = "test-investigatorId"
        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val email = Email("test@test.com")
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val investigator = Investigator(
            id = investigatorId,
            firstName = firstName,
            lastName = lastName,
            company = company,
            team = team,
            email = email,
            officePhoneNumber = officePhoneNumber,
            mobilePhoneNumber = mobilePhoneNumber
        )
        val studyId = "test-studyId"
        val role = "test-role"
        val relation = InvestigatorStudyRelation(
            email = email,
            studyId = studyId,
            role = role
        )

        coEvery {
            updateRoleOutPort.updateRole(investigatorId, studyId, role)
        } returns role

        coEvery {
            getInvestigatorOutPort.getInvestigator(investigatorId)
        } returns investigator

        coEvery {
            updateRelationOutPort.updateRelation(email.value, studyId, role)
        } returns relation

        val result = updateInvestigatorService.updateInvestigatorRole(
            investigatorId,
            studyId,
            role
        )

        assertEquals(result, role)
    }
}
