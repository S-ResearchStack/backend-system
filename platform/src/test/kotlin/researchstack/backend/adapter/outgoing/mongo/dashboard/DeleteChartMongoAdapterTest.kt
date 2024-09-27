package researchstack.backend.adapter.outgoing.mongo.dashboard

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.DashboardTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.ChartRepository

@ExperimentalCoroutinesApi
internal class DeleteChartMongoAdapterTest {
    private val chartRepository = mockk<ChartRepository>()
    private val adapter = DeleteChartMongoAdapter(
        chartRepository
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteChart should work properly`() = runTest {
        val chartId = DashboardTestUtil.chartId

        every {
            chartRepository.deleteById(chartId)
        } answers {
            Mono.empty()
        }

        assertDoesNotThrow {
            adapter.deleteChart(chartId)
        }
    }
}
