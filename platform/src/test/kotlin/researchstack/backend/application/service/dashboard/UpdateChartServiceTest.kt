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
import researchstack.backend.application.port.outgoing.dashboard.GetChartOutPort
import researchstack.backend.application.port.outgoing.dashboard.UpdateChartOutPort

@ExperimentalCoroutinesApi
internal class UpdateChartServiceTest {
    private val getChartOutPort = mockk<GetChartOutPort>()
    private val updateChartOutPort = mockk<UpdateChartOutPort>()
    private val updateChartService = UpdateChartService(
        getChartOutPort,
        updateChartOutPort
    )

    private val chartId = DashboardTestUtil.chartId
    private val dashboardId = DashboardTestUtil.dashboardId
    private val studyId = DashboardTestUtil.studyId

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateChart should work properly`() = runTest {
        val command = DashboardTestUtil.getUpdateChartCommand()
        val chartDomain = DashboardTestUtil.getChartDomain(chartId)
        coEvery {
            getChartOutPort.getChart(chartId)
        } returns chartDomain

        coEvery {
            updateChartOutPort.updateChart(any())
        } returns Unit

        assertDoesNotThrow {
            updateChartService.updateChart(
                studyId,
                dashboardId,
                chartId,
                command
            )
        }
    }
}
