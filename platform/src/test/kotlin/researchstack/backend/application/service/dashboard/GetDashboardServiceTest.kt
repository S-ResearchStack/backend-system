package researchstack.backend.application.service.dashboard

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.dashboard.GetDashboardOutPort
import researchstack.backend.domain.dashboard.Dashboard
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetDashboardServiceTest {
    private val getDashboardOutPort = mockk<GetDashboardOutPort>()
    private val getDashboardService = GetDashboardService(
        getDashboardOutPort
    )

    private val studyId = "test-study-id"
    private val dashboardId = "test-dashboard-id"
    private val title = "test-title"

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDashboard should work properly`() = runTest {
        coEvery {
            getDashboardOutPort.getDashboard(dashboardId)
        } returns Dashboard(
            dashboardId,
            title
        )

        val dashboard = getDashboardService.getDashboard(studyId, dashboardId)
        assertEquals(dashboardId, dashboard.id)
        assertEquals(title, dashboard.title)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDashboardList should work properly`() = runTest {
        coEvery {
            getDashboardOutPort.getDashboardList()
        } returns listOf(
            Dashboard(
                dashboardId,
                title
            )
        )

        val dashboardList = getDashboardService.getDashboardList(studyId)
        assertEquals(1, dashboardList.size)
    }
}
