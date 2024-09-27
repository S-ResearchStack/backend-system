package researchstack.backend.adapter.outgoing.mongo.dashboard

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.DashboardTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ChartRepository
import researchstack.backend.application.exception.InternalServerException

@ExperimentalCoroutinesApi
internal class CreateChartMongoAdapterTest {
    private val chartRepository = mockk<ChartRepository>()
    private val adapter = CreateChartMongoAdapter(
        chartRepository
    )
    private val chartId = DashboardTestUtil.chartId

    @Test
    @Tag(POSITIVE_TEST)
    fun `createChart should work properly`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain(chartId)
        val chartEntity = chartDomain.toEntity()

        every {
            chartRepository.insert(chartEntity)
        } answers {
            chartEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createChart(chartDomain)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createChart should throw InternalServerException if chart id is null`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain()
        val chartEntity = chartDomain.toEntity()

        every {
            chartRepository.insert(chartEntity)
        } answers {
            chartEntity.toMono()
        }

        assertThrows<InternalServerException> {
            adapter.createChart(chartDomain)
        }
    }
}
