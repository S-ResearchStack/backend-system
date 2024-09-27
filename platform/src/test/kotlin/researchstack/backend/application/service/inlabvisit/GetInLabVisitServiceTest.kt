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
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetInLabVisitServiceTest {
    private val getInLabVisitOutPort = mockk<GetInLabVisitOutPort>()
    private val getInLabVisitService = GetInLabVisitService(
        getInLabVisitOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInLabVisitList should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val inLabVisitList = listOf(InLabVisitTestUtil.getInLabVisit())
        val totalCount = inLabVisitList.size.toLong()

        coEvery {
            getInLabVisitOutPort.getInLabVisitList()
        } returns inLabVisitList

        coEvery {
            getInLabVisitOutPort.getInLabVisitListCount()
        } returns totalCount

        val inLabVisitListResponse = assertDoesNotThrow {
            getInLabVisitService.getInLabVisitList(studyId)
        }

        assertEquals(inLabVisitListResponse.list.size, inLabVisitList.size)
        assertEquals(inLabVisitListResponse.totalCount, totalCount)
    }
}
