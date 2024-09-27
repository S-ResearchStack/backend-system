package researchstack.backend.application.service.investigator

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.casbin.GetRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class GetInvestigatorServiceTest {
    private val getInvestigatorOutPort = mockk<GetInvestigatorOutPort>()
    private val getRoleOutPort = mockk<GetRoleOutPort>()

    private val getInvestigatorService = GetInvestigatorService(getInvestigatorOutPort, getRoleOutPort)

    @Tag(POSITIVE_TEST)
    @Test
    fun `getInvestigator should return investigator`() = runTest {
        val investigatorId = "test-investigator"
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
        val roles = listOf("test-role")

        coEvery {
            getInvestigatorOutPort.getInvestigator(investigatorId)
        } returns investigator

        coEvery {
            getRoleOutPort.getRoles(investigatorId)
        } returns roles

        val result = getInvestigatorService.getInvestigator(investigatorId)
        assertEquals(result.roles, roles)
        assertTrue(EqualsBuilder.reflectionEquals(result, investigator.apply { this.roles = roles }))
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `getInvestigators should return investigators`() = runTest {
        val id = "test-investigator"
        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val email = Email("test@test.com")
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val investigators = listOf(
            Investigator(
                id = id,
                firstName = firstName,
                lastName = lastName,
                company = company,
                team = team,
                email = email,
                officePhoneNumber = officePhoneNumber,
                mobilePhoneNumber = mobilePhoneNumber
            )
        )
        val studyId = "test-studyId"
        val roles = listOf("test-role")

        coEvery {
            getInvestigatorOutPort.getInvestigatorsByStudyId(studyId)
        } returns investigators

        coEvery {
            getRoleOutPort.getRolesInStudy(id, studyId)
        } returns roles

        val result = getInvestigatorService.getInvestigators(studyId)

        assertEquals(result.size, investigators.size)

        for (i: Int in result.indices) {
            assertEquals(result[i].roles, roles)
            assertTrue(EqualsBuilder.reflectionEquals(result[i], investigators[i].apply { this.roles = roles }))
        }
    }
}
