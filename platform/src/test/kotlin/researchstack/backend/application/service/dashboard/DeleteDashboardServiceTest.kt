package researchstack.backend.application.service.dashboard

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.dashboard.DeleteDashboardOutPort

@ExperimentalCoroutinesApi
internal class DeleteDashboardServiceTest {
    private val deleteDashboardOutPort = mockk<DeleteDashboardOutPort>()
    private val deleteDashboardService = DeleteDashboardService(
        deleteDashboardOutPort
    )

    private val studyId = "test-study-id"
    private val dashboardId = "test-dashboard-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteDashboard should work properly`() = runTest {
        coEvery {
            deleteDashboardOutPort.deleteDashboard(dashboardId)
        } returns Unit

        assertDoesNotThrow {
            deleteDashboardService.deleteDashboard(
                studyId,
                dashboardId
            )
        }
    }
}
