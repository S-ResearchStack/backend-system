package researchstack.backend.application.service.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.casbin.DeleteRoleOutPort
import researchstack.backend.application.port.outgoing.study.DeleteSubjectNumberOutPort
import researchstack.backend.application.port.outgoing.study.DeleteSubjectStudyRelationOutPort
import researchstack.backend.application.port.outgoing.studydata.DeleteSubjectInfoOutPort
import researchstack.backend.application.service.permission.CheckPermissionService

@ExperimentalCoroutinesApi
internal class WithdrawFromStudyServiceTest {
    private val deleteSubjectNumberOutPort = mockk<DeleteSubjectNumberOutPort>()
    private val deleteSubjectStudyRelationOutPort = mockk<DeleteSubjectStudyRelationOutPort>()
    private val deleteSubjectInfoOutPort = mockk<DeleteSubjectInfoOutPort>()
    private val deleteRoleOutPort = mockk<DeleteRoleOutPort>(relaxed = true)

    private val withdrawFromStudyService = WithdrawFromStudyService(
        deleteSubjectNumberOutPort,
        deleteSubjectStudyRelationOutPort,
        deleteSubjectInfoOutPort,
        deleteRoleOutPort,
        CheckPermissionService()
    )
    private val studyId = "test-study-id"
    private val userId = "test-subject-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `withdrawFromStudyOutPort should work properly`() = runTest {
        coEvery {
            deleteSubjectNumberOutPort.deleteSubjectNumber(subjectId = userId, studyId = studyId)
        } returns Unit

        coEvery {
            deleteSubjectStudyRelationOutPort.deleteSubjectStudyRelation(subjectId = userId, studyId = studyId)
        } returns Unit

        coEvery {
            deleteSubjectInfoOutPort.deleteSubjectInfo(subjectId = userId, studyId = studyId)
        } returns Unit

        assertDoesNotThrow {
            withdrawFromStudyService.withdrawFromStudy(userId, studyId)
        }
    }
}
