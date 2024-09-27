package researchstack.backend.application.service.investigator

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorCommand
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.investigator.RegisterInvestigatorOutPort
import researchstack.backend.application.service.mapper.toDomain
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.InvestigatorStudyRelation
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class RegisterInvestigatorServiceTest {
    private val registerInvestigatorOutPort = mockk<RegisterInvestigatorOutPort>()
    private val getRelationOutPort = mockk<GetInvestigatorStudyRelationOutPort>()
    private val addRoleOutPort = mockk<AddRoleOutPort>()

    private val registerInvestigatorService = RegisterInvestigatorService(
        registerInvestigatorOutPort = registerInvestigatorOutPort,
        getRelationOutPort = getRelationOutPort,
        addRoleOutPort = addRoleOutPort
    )

    @Tag(POSITIVE_TEST)
    @Test
    fun `registerInvestigator should return investigator`() = runTest {
        val investigatorId = "test-investigator"
        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val email = "test@test.com"
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val command = RegisterInvestigatorCommand(
            firstName = firstName,
            lastName = lastName,
            company = company,
            team = team,
            officePhoneNumber = officePhoneNumber,
            mobilePhoneNumber = mobilePhoneNumber
        )
        val studyId = "test-studyId"
        val role = "test-role"
        val relations = listOf(
            InvestigatorStudyRelation(
                email = Email(email),
                studyId = studyId,
                role = role
            )
        )

        val investigator = command.toDomain(investigatorId, email)

        coEvery {
            registerInvestigatorOutPort.registerInvestigator(investigator)
        } returns investigator

        coEvery {
            getRelationOutPort.getRelationsByEmail(email)
        } returns relations

        coEvery {
            addRoleOutPort.addRole(investigatorId, studyId, role)
        } returns true

        coEvery {
            addRoleOutPort.addRolesForMyself(investigatorId)
        } returns Unit

        val result = registerInvestigatorService.registerInvestigator(investigatorId, email, command)
        assertTrue {
            EqualsBuilder.reflectionEquals(result, command.toDomain(investigatorId, email))
        }
    }
}
