package researchstack.backend.application.service.inlabvisit

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.InLabVisitTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.inlabvisit.DeleteInLabVisitOutPort

@ExperimentalCoroutinesApi
internal class DeleteInLabVisitServiceTest {
    private val deleteInLabVisitOutPort = mockk<DeleteInLabVisitOutPort>()
    private val deleteInLabVisitService = DeleteInLabVisitService(
        deleteInLabVisitOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteInLabVisit should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val inLabVisitId = InLabVisitTestUtil.id

        coEvery {
            deleteInLabVisitOutPort.deleteInLabVisit(inLabVisitId)
        } returns Unit

        assertDoesNotThrow {
            deleteInLabVisitService.deleteInLabVisit(studyId, inLabVisitId)
        }
    }
}
