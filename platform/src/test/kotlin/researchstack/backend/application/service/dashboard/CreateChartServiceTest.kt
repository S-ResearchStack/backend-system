package researchstack.backend.application.service.dashboard

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.DashboardTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.dashboard.CreateChartOutPort
import researchstack.backend.application.service.mapper.toDomain

@ExperimentalCoroutinesApi
internal class CreateChartServiceTest {
    private val createChartOutPort = mockk<CreateChartOutPort>()
    private val createChartService = CreateChartService(
        createChartOutPort
    )

    private val studyId = DashboardTestUtil.studyId
    private val dashboardId = DashboardTestUtil.dashboardId
    private val chartId = DashboardTestUtil.chartId

    @Test
    @Tag(POSITIVE_TEST)
    fun `createChart should work properly`() = runTest {
        val command = DashboardTestUtil.getCreateChartCommand()

        coEvery {
            createChartOutPort.createChart(
                command.toDomain(
                    studyId,
                    dashboardId
                )
            )
        } returns chartId

        assertDoesNotThrow {
            createChartService.createChart(
                studyId,
                dashboardId,
                command
            )
        }
    }
}
