package researchstack.backend.adapter.outgoing.mongo.dashboard

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.DashboardRepository
import researchstack.backend.domain.dashboard.Dashboard
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetDashboardMongoAdapterTest {
    private val dashboardRepository = mockk<DashboardRepository>()
    private val adapter = GetDashboardMongoAdapter(
        dashboardRepository
    )

    private val dashboardId = "test-dashboard-id"
    private val title = "test-title"

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDashboard should work properly`() = runTest {
        val dashboardDomain = Dashboard(
            dashboardId,
            title
        )
        val dashboardEntity = dashboardDomain.toEntity()

        every {
            dashboardRepository.findById(dashboardId)
        } answers {
            dashboardEntity.toMono()
        }

        val dashboard = adapter.getDashboard(dashboardId)
        assertEquals(dashboardId, dashboard.id)
        assertEquals(title, dashboard.title)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDashboardList should work properly`() = runTest {
        val dashboardDomain = Dashboard(
            dashboardId,
            title
        )
        val dashboardEntity = dashboardDomain.toEntity()

        every {
            dashboardRepository.findAll()
        } answers {
            listOf(
                dashboardEntity
            ).toFlux()
        }

        val dashboardList = adapter.getDashboardList()
        assertEquals(1, dashboardList.size)
    }
}
