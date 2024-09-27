package researchstack.backend.application.service.subject

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.SubjectTestUtil
import researchstack.backend.application.port.outgoing.studydata.GetSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.subject.GetSubjectProfileOutPort
import researchstack.backend.application.service.permission.CheckPermissionService
import researchstack.backend.domain.subject.Subject.SubjectId
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.SubjectStatus

@ExperimentalCoroutinesApi
internal class GetSubjectServiceTest {
    private val getSubjectInfoOutPort = mockk<GetSubjectInfoOutPort>()
    private val getSubjectProfileOutPort = mockk<GetSubjectProfileOutPort>()
    private val getUserProfileService = GetSubjectProfileService(
        getSubjectInfoOutPort,
        getSubjectProfileOutPort,
        CheckPermissionService()
    )
    private val studyId = "test-study-id"
    private val subjectNumber = "test-subject-number"

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUserProfile should work properly`() = runTest {
        val userId = SubjectTestUtil.subjectId
        val userProfile = SubjectTestUtil.createRegisterSubjectCommand(userId).toDomain()
        coEvery {
            getSubjectProfileOutPort.getSubjectProfile(SubjectId.from(userId))
        } returns userProfile
        val actual = getUserProfileService.getSubjectProfile(SubjectId.from(userId))
        SubjectTestUtil.compareSubjectProfile(userProfile, actual)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getUserProfile should throw IllegalArgumentException if userId is null`() = runTest {
        val userId = null
        assertThrows<IllegalArgumentException> {
            getUserProfileService.getSubjectProfile(SubjectId.from(userId))
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUserStatus should work properly`() = runTest {
        val userId = SubjectTestUtil.subjectId
        coEvery {
            getSubjectProfileOutPort.getSubjectNumber(studyId, userId)
        } returns subjectNumber
        coEvery {
            getSubjectInfoOutPort.getSubjectInfo(studyId, subjectNumber)
        } returns SubjectInfo(
            studyId,
            subjectNumber,
            SubjectStatus.COMPLETED,
            userId
        )
        val actual = getUserProfileService.getSubjectStatus(
            SubjectId.from(userId),
            studyId
        )
        org.junit.jupiter.api.Assertions.assertEquals(
            SubjectStatus.COMPLETED,
            actual.status
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getUserStatus should throw IllegalArgumentException if userId is null`() = runTest {
        val userId = null
        assertThrows<IllegalArgumentException> {
            getUserProfileService.getSubjectStatus(
                SubjectId.from(userId),
                studyId
            )
        }
    }
}
