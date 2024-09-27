package researchstack.backend.application.service.dashboard

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.DashboardTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.dashboard.GetChartOutPort
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetChartServiceTest {
    private val getChartOutPort = mockk<GetChartOutPort>()
    private val getChartService = GetChartService(
        getChartOutPort
    )

    private val chartId = DashboardTestUtil.chartId
    private val dashboardId = DashboardTestUtil.dashboardId
    private val studyId = DashboardTestUtil.studyId

    @Test
    @Tag(POSITIVE_TEST)
    fun `getChart should work properly`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain(chartId)
        coEvery {
            getChartOutPort.getChart(chartId)
        } returns chartDomain

        val chart = getChartService.getChart(
            studyId,
            dashboardId,
            chartId
        )
        assertEquals(chartId, chart.id)
        assertEquals(dashboardId, chart.dashboardId)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getChartList should work properly`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain(chartId)
        coEvery {
            getChartOutPort.getChartList(
                dashboardId
            )
        } returns listOf(
            chartDomain
        )

        val chartList = getChartService.getChartList(
            studyId,
            dashboardId
        )
        assertEquals(1, chartList.size)
    }
}
