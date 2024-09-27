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
import researchstack.backend.application.port.outgoing.inlabvisit.CreateInLabVisitOutPort

@ExperimentalCoroutinesApi
internal class CreateInLabVisitServiceTest {
    private val createInLabVisitOutPort = mockk<CreateInLabVisitOutPort>()
    private val createInLabVisitService = CreateInLabVisitService(
        createInLabVisitOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `createInLabVisit should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val creatorId = InLabVisitTestUtil.creatorId
        val inLabVisit = InLabVisitTestUtil.getInLabVisit()
        val command = InLabVisitTestUtil.getCreateInLabVisitCommand()

        coEvery {
            createInLabVisitOutPort.createInLabVisit(any())
        } returns inLabVisit

        assertDoesNotThrow {
            createInLabVisitService.createInLabVisit(
                studyId = studyId,
                investigatorId = creatorId,
                command = command
            )
        }
    }
}
