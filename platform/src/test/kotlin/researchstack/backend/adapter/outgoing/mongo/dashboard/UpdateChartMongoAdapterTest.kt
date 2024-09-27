package researchstack.backend.adapter.outgoing.mongo.dashboard

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
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

@ExperimentalCoroutinesApi
internal class UpdateChartMongoAdapterTest {
    private val chartRepository = mockk<ChartRepository>()
    private val adapter = UpdateChartMongoAdapter(
        chartRepository
    )

    private val chartId = DashboardTestUtil.chartId

    private val serviceRequestContext = ServiceRequestContext.builder(
        HttpRequest.of(HttpMethod.GET, "/test")
    ).build()

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateChart should work properly`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain(chartId)
        val chartEntity = chartDomain.toEntity()
        every {
            chartRepository.findById(chartId)
        } answers {
            chartEntity.toMono()
        }

        every {
            chartRepository.save(chartEntity)
        } answers {
            chartEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.updateChart(chartDomain)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateChart should throw IllegalArgumentException when the given id is null`() = runTest {
        val chartDomain = DashboardTestUtil.getChartDomain()

        assertThrows<IllegalArgumentException> {
            adapter.updateChart(chartDomain)
        }
    }
}
