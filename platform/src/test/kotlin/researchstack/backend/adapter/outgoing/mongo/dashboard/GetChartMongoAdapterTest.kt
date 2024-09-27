package researchstack.backend.adapter.outgoing.mongo.dashboard

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.DashboardTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ChartRepository
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetChartMongoAdapterTest {
    private val chartRepository = mockk<ChartRepository>()
    private val adapter = GetChartMongoAdapter(
        chartRepository
    )

    private val chartId = DashboardTestUtil.chartId
    private val dashboardId = DashboardTestUtil.dashboardId

    @Test
    @Tag(POSITIVE_TEST)
    fun `getChart should work properly`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain(chartId)
        val chartEntity = chartDomain.toEntity()

        every {
            chartRepository.findById(chartId)
        } answers {
            chartEntity.toMono()
        }

        val chart = adapter.getChart(chartId)
        assertEquals(chartId, chart.id)
        assertEquals(dashboardId, chart.dashboardId)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getChartList should work properly`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain(chartId)
        val chartEntity = chartDomain.toEntity()

        every {
            chartRepository.findAllByDashboardId(dashboardId)
        } answers {
            listOf(
                chartEntity
            ).toFlux()
        }

        val chartList = adapter.getChartList(dashboardId)
        assertEquals(1, chartList.size)
    }
}
