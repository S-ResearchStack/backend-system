package researchstack.backend.application.service.dashboard

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardCommand
import researchstack.backend.application.port.outgoing.dashboard.CreateDashboardOutPort
import researchstack.backend.application.service.mapper.toDomain

@ExperimentalCoroutinesApi
internal class CreateDashboardServiceTest {
    private val createDashboardOutPort = mockk<CreateDashboardOutPort>()
    private val createDashboardService = CreateDashboardService(
        createDashboardOutPort
    )

    private val studyId = "test-study-id"
    private val title = "test-title"
    private val dashboardId = "test-dashboard-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `createDashboard should work properly`() = runTest {
        val command = CreateDashboardCommand(
            title
        )

        coEvery {
            createDashboardOutPort.createDashboard(
                command.toDomain(studyId)
            )
        } returns dashboardId

        assertDoesNotThrow {
            createDashboardService.createDashboard(
                studyId,
                command
            )
        }
    }
}
