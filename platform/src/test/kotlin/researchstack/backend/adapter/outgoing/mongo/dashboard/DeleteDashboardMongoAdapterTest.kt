package researchstack.backend.adapter.outgoing.mongo.dashboard

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.DashboardRepository

@ExperimentalCoroutinesApi
internal class DeleteDashboardMongoAdapterTest {
    private val dashboardRepository = mockk<DashboardRepository>()
    private val adapter = DeleteDashboardMongoAdapter(
        dashboardRepository
    )

    private val dashboardId = "test-dashboard-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteDashboard should work properly`() = runTest {
        every {
            dashboardRepository.deleteById(dashboardId)
        } answers {
            Mono.empty()
        }

        assertDoesNotThrow {
            adapter.deleteDashboard(dashboardId)
        }
    }
}
