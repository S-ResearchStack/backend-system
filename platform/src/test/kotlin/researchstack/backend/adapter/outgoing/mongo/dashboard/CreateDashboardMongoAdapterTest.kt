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
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.DashboardRepository
import researchstack.backend.application.exception.InternalServerException
import researchstack.backend.domain.dashboard.Dashboard

@ExperimentalCoroutinesApi
internal class CreateDashboardMongoAdapterTest {
    private val dashboardRepository = mockk<DashboardRepository>()
    private val adapter = CreateDashboardMongoAdapter(
        dashboardRepository
    )

    private val dashboardId = "test-dashboard-id"
    private val title = "test-title"

    @Test
    @Tag(POSITIVE_TEST)
    fun `createDashboard should work properly`() = runTest {
        val dashboardDomain = Dashboard(
            dashboardId,
            title
        )
        val dashboardEntity = dashboardDomain.toEntity()

        every {
            dashboardRepository.insert(dashboardEntity)
        } answers {
            dashboardEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createDashboard(dashboardDomain)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createDashboard should throw InternalServerException if dashboard id is null`() = runTest {
        val dashboardDomain = Dashboard(
            null,
            title
        )
        val dashboardEntity = dashboardDomain.toEntity()

        every {
            dashboardRepository.insert(dashboardEntity)
        } answers {
            dashboardEntity.toMono()
        }

        assertThrows<InternalServerException> {
            adapter.createDashboard(dashboardDomain)
        }
    }
}
