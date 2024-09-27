package researchstack.backend.application.service.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.incoming.study.GetStudyUseCase
import researchstack.backend.application.port.outgoing.casbin.CreateStudyPolicyOutPort
import researchstack.backend.application.port.outgoing.casbin.GetRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.AddInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.application.port.outgoing.study.CreateStudyOutPort
import researchstack.backend.application.port.outgoing.study.CreateSubjectNumberGeneratorOutPort
import researchstack.backend.application.port.outgoing.studydata.AddStudyDataInfoOutPort
import researchstack.backend.config.STUDY_DATA_ROOT_ID
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator
import researchstack.backend.domain.investigator.InvestigatorStudyRelation
import researchstack.backend.domain.studydata.StudyDataFolder

@ExperimentalCoroutinesApi
internal class CreateStudyServiceTest {
    private val createSubjectNumberGeneratorOutPort = mockk<CreateSubjectNumberGeneratorOutPort>()
    private val createStudyOutPort = mockk<CreateStudyOutPort>()
    private val createStudyPolicyOutPort = mockk<CreateStudyPolicyOutPort>()
    private val getStudyUseCase = mockk<GetStudyUseCase>()
    private val addStudyDataInfoOutPort = mockk<AddStudyDataInfoOutPort>(relaxed = true)
    private val getInvestigatorOutPort = mockk<GetInvestigatorOutPort>()
    private val getRoleOutPort = mockk<GetRoleOutPort>()
    private val addInvestigatorStudyRelationOutPort = mockk<AddInvestigatorStudyRelationOutPort>()
    private val createStudyService = CreateStudyService(
        createSubjectNumberGeneratorOutPort,
        createStudyOutPort,
        createStudyPolicyOutPort,
        getStudyUseCase,
        addStudyDataInfoOutPort,
        getInvestigatorOutPort,
        getRoleOutPort,
        addInvestigatorStudyRelationOutPort
    )

    private val studyId = "test-study-id"
    private val investigatorId = "test-investigator-id"
    private val participationCode = "secret"
    private val studyDataFolder = StudyDataFolder(
        name = studyId,
        studyId = studyId,
        parentId = STUDY_DATA_ROOT_ID
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `createStudy should work properly`() = runTest {
        val command = StudyTestUtil.createCreateStudyCommand(studyId)
        val email = Email("test@test.com")
        val roles = listOf("Admin")
        val investigator = Investigator(
            id = investigatorId,
            email = email,
            firstName = "firstName",
            lastName = "lastName",
            company = "company",
            team = "team",
            officePhoneNumber = "phoneNumber",
            mobilePhoneNumber = "phoneNumber",
            roles = roles
        )

        coEvery {
            createSubjectNumberGeneratorOutPort.createSubjectNumberGenerator(command.id)
        } returns Unit
        coEvery {
            createStudyOutPort.createStudy(command.toDomain())
        } returns studyId
        coEvery {
            createStudyPolicyOutPort.createStudyPolicies(investigatorId, command.id)
        } returns Unit
        coEvery {
            addStudyDataInfoOutPort.addStudyDataInfo(studyDataFolder)
        } returns Unit
        coEvery {
            getStudyUseCase.getStudyByParticipationCode(participationCode)
        } throws NotFoundException()
        coEvery {
            getInvestigatorOutPort.getInvestigator(investigatorId)
        } returns investigator
        coEvery {
            getRoleOutPort.getRolesInStudy(investigatorId, studyId)
        } returns roles
        coEvery {
            addInvestigatorStudyRelationOutPort.addRelation(any())
        } returns InvestigatorStudyRelation(email, studyId, roles.first())

        assertDoesNotThrow {
            createStudyService.createStudy(investigatorId, command)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `validateParticipationCode should work properly when duplicate code does not exist`() = runTest {
        coEvery {
            getStudyUseCase.getStudyByParticipationCode(participationCode)
        } throws NotFoundException()

        assertDoesNotThrow {
            createStudyService.validateParticipationCode(participationCode)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `validateParticipationCode should throw exception when duplicate code exists`() = runTest {
        coEvery {
            getStudyUseCase.getStudyByParticipationCode(participationCode)
        } returns StudyTestUtil.studyResponse

        assertThrows<AlreadyExistsException> {
            createStudyService.validateParticipationCode(participationCode)
        }
    }
}
