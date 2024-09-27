package researchstack.backend.application.service.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.casbin.jcasbin.main.Enforcer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.io.ClassPathResource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.adapter.outgoing.casbin.RoleConstant
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.outgoing.study.GetStudyOutPort

@ExperimentalCoroutinesApi
internal class GetStudyServiceTest {
    private val enforcer = Enforcer(ClassPathResource("casbin/model.conf").uri.path)
    private val getStudyOutPort = mockk<GetStudyOutPort>()
    private val getStudyService = GetStudyService(enforcer, getStudyOutPort)

    private val id = "0"
    private val participationCode = "secret"
    private val firstStudy = StudyTestUtil.createDummyStudy(id)

    @BeforeEach
    fun setUp() {
        coEvery { getStudyOutPort.getStudy(id) } returns firstStudy
        coEvery { getStudyOutPort.getStudyByParticipationCode(participationCode) } returns firstStudy
        enforcer.clearPolicy()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `get study by id properly`() = runTest {
        val result = getStudyService.getStudy(id)

        assertEquals(result.id, id)
        assertEquals(result.studyInfoResponse.name, firstStudy.studyInfo.name)
        assertEquals(result.studyInfoResponse.description, firstStudy.studyInfo.description)
        assertEquals(result.studyInfoResponse.participationApprovalType, firstStudy.studyInfo.participationApprovalType)
        assertEquals(result.studyInfoResponse.scope, firstStudy.studyInfo.scope)
        assertEquals(result.studyInfoResponse.stage, firstStudy.studyInfo.stage)
        assertEquals(result.studyInfoResponse.logoUrl, firstStudy.studyInfo.logoUrl)
        assertEquals(result.studyInfoResponse.imageUrl, firstStudy.studyInfo.imageUrl)
        assertEquals(result.irbInfoResponse.decisionType, firstStudy.irbInfo.irbDecisionType)
        assertEquals(result.studyInfoResponse.organization, firstStudy.studyInfo.organization)
        assertEquals(result.studyInfoResponse.duration, firstStudy.studyInfo.duration)
        assertEquals(result.studyInfoResponse.period, firstStudy.studyInfo.period)
        assertEquals(result.studyInfoResponse.requirements, firstStudy.studyInfo.requirements)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `get study returns Exception when it's id was empty`() = runTest {
        coEvery { getStudyOutPort.getStudy("") } throws NotFoundException("Study(id: '') does not exist")
        assertThrows<NotFoundException> {
            getStudyService.getStudy("")
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `get study by participation code properly`() = runTest {
        val result = getStudyService.getStudyByParticipationCode(participationCode)

        assertEquals(result.id, id)
        assertEquals(result.studyInfoResponse.name, firstStudy.studyInfo.name)
        assertEquals(result.studyInfoResponse.description, firstStudy.studyInfo.description)
        assertEquals(result.studyInfoResponse.participationApprovalType, firstStudy.studyInfo.participationApprovalType)
        assertEquals(result.studyInfoResponse.scope, firstStudy.studyInfo.scope)
        assertEquals(result.studyInfoResponse.stage, firstStudy.studyInfo.stage)
        assertEquals(result.studyInfoResponse.logoUrl, firstStudy.studyInfo.logoUrl)
        assertEquals(result.studyInfoResponse.imageUrl, firstStudy.studyInfo.imageUrl)
        assertEquals(result.irbInfoResponse.decisionType, firstStudy.irbInfo.irbDecisionType)
        assertEquals(result.studyInfoResponse.organization, firstStudy.studyInfo.organization)
        assertEquals(result.studyInfoResponse.duration, firstStudy.studyInfo.duration)
        assertEquals(result.studyInfoResponse.period, firstStudy.studyInfo.period)
        assertEquals(result.studyInfoResponse.requirements, firstStudy.studyInfo.requirements)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `get study by empty participation code returns Exception`() = runTest {
        coEvery { getStudyOutPort.getStudyByParticipationCode("") } throws NotFoundException()
        assertThrows<NotFoundException> {
            getStudyService.getStudyByParticipationCode("")
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyListByUser should return accessible study list only`() = runTest {
        val userId = "u1"
        val accessibleStudyIds = listOf("s1", "s3", "s5")
        accessibleStudyIds.forEach { studyId ->
            enforcer.addPolicy(userId, studyId, RoleConstant.RESOURCE_STUDY, "read")
        }
        coEvery { getStudyOutPort.getStudyList() } returns
            listOf(
                StudyTestUtil.createDummyStudy("s1"),
                StudyTestUtil.createDummyStudy("s2"),
                StudyTestUtil.createDummyStudy("s3"),
                StudyTestUtil.createDummyStudy("s4"),
                StudyTestUtil.createDummyStudy("s5")
            )

        val response = getStudyService.getStudyListByUser(userId)
        assertEquals(3, response.size)
        accessibleStudyIds.forEach { studyId ->
            assertTrue { response.find { it.id == studyId } != null }
        }
    }
}
