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
import researchstack.backend.application.port.outgoing.inlabvisit.GetInLabVisitOutPort
import researchstack.backend.application.port.outgoing.inlabvisit.UpdateInLabVisitOutPort

@ExperimentalCoroutinesApi
class UpdateInLabVisitServiceTest {
    private val getInLabVisitOutPort = mockk<GetInLabVisitOutPort>()
    private val updateInLabVisitOutPort = mockk<UpdateInLabVisitOutPort>()
    private val updateInLabVisitService = UpdateInLabVisitService(
        getInLabVisitOutPort,
        updateInLabVisitOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInLabVisit should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val id = InLabVisitTestUtil.id
        val command = InLabVisitTestUtil.getUpdateInLabVisitCommand()
        val inLabVisit = InLabVisitTestUtil.getInLabVisit()

        coEvery {
            getInLabVisitOutPort.getInLabVisit(id)
        } returns inLabVisit

        coEvery {
            updateInLabVisitOutPort.updateInLabVisit(any())
        } returns Unit

        assertDoesNotThrow {
            updateInLabVisitService.updateInLabVisit(studyId, id, command)
        }
    }
}
