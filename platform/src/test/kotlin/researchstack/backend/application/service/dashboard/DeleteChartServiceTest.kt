package researchstack.backend.application.service.dashboard

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.DashboardTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.dashboard.DeleteChartOutPort

@ExperimentalCoroutinesApi
internal class DeleteChartServiceTest {
    private val deleteChartOutPort = mockk<DeleteChartOutPort>()
    private val deleteChartService = DeleteChartService(
        deleteChartOutPort
    )

    private val chartId = DashboardTestUtil.chartId
    private val dashboardId = DashboardTestUtil.dashboardId
    private val studyId = DashboardTestUtil.studyId

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteChart should work properly`() = runTest {
        coEvery {
            deleteChartOutPort.deleteChart(chartId)
        } returns Unit

        assertDoesNotThrow {
            deleteChartService.deleteChart(
                studyId,
                dashboardId,
                chartId
            )
        }
    }
}
