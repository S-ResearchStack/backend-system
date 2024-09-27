package researchstack.backend.adapter.incoming.rest.dashboard

import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.DashboardTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.dashboard.CreateChartUseCase
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardCommand
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardUseCase
import researchstack.backend.application.port.incoming.dashboard.DeleteChartUseCase
import researchstack.backend.application.port.incoming.dashboard.DeleteDashboardUseCase
import researchstack.backend.application.port.incoming.dashboard.GetChartUseCase
import researchstack.backend.application.port.incoming.dashboard.GetDashboardUseCase
import researchstack.backend.application.port.incoming.dashboard.UpdateChartUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.dashboard.Dashboard
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class DashboardRestControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val createDashboardUseCase = mockk<CreateDashboardUseCase>()
    private val getDashboardUseCase = mockk<GetDashboardUseCase>()
    private val deleteDashboardUseCase = mockk<DeleteDashboardUseCase>()
    private val createChartUseCase = mockk<CreateChartUseCase>()
    private val updateChartUseCase = mockk<UpdateChartUseCase>()
    private val getChartUseCase = mockk<GetChartUseCase>()
    private val deleteChartUseCase = mockk<DeleteChartUseCase>()

    private val dashboardRestController = DashboardRestController(
        createDashboardUseCase,
        getDashboardUseCase,
        deleteDashboardUseCase,
        createChartUseCase,
        updateChartUseCase,
        getChartUseCase,
        deleteChartUseCase
    )

    private val chartId = "test-chart-id"
    private val dashboardId = "test-dashboard-id"
    private val studyId = "test-study-id"
    private val title = "heart-rate"
    private val userId = "test-subject-id"

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createDashboard should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = CreateDashboardCommand(
            title
        )

        coEvery {
            createDashboardUseCase.createDashboard(
                studyId,
                command
            )
        } returns dashboardId

        val res = dashboardRestController.createDashboard(
            studyId,
            command
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createDashboard should throw IllegalArgumentException if userId is empty `(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = CreateDashboardCommand(
            title
        )

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.createDashboard(
                studyId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createDashboard should throw IllegalArgumentException if studyId is empty `(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = CreateDashboardCommand(
            title
        )

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.createDashboard(
                studyId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDashboard should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getDashboardUseCase.getDashboard(
                studyId,
                dashboardId
            )
        } returns Dashboard(
            dashboardId,
            title
        )

        val res = dashboardRestController.getDashboard(
            studyId,
            dashboardId
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getDashboard should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getDashboard(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getDashboard should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getDashboard(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getDashboard should throw IllegalArgumentException if dashboardId is empty`(dashboardId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getDashboard(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_DASHBOARD_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDashboardList should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getDashboardUseCase.getDashboardList(
                studyId
            )
        } returns listOf(
            Dashboard(
                dashboardId,
                title
            )
        )

        val res = dashboardRestController.getDashboardList(
            studyId
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getDashboardList should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getDashboardList(
                studyId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getDashboardList should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getDashboardList(
                studyId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteDashboard should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            deleteDashboardUseCase.deleteDashboard(
                studyId,
                dashboardId
            )
        } returns Unit

        val res = dashboardRestController.deleteDashboard(
            studyId,
            dashboardId
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteDashboard should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.deleteDashboard(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteDashboard should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.deleteDashboard(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteDashboard should throw IllegalArgumentException if dashboardId is empty`(dashboardId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.deleteDashboard(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_DASHBOARD_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createChart should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getCreateChartCommand()

        coEvery {
            createChartUseCase.createChart(
                studyId,
                dashboardId,
                command
            )
        } returns chartId

        val res = dashboardRestController.createChart(
            studyId,
            dashboardId,
            command
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createChart should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getCreateChartCommand()
        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.createChart(
                studyId,
                dashboardId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createChart should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getCreateChartCommand()
        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.createChart(
                studyId,
                dashboardId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createChart should throw IllegalArgumentException if dashboardId is empty`(dashboardId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getCreateChartCommand()
        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.createChart(
                studyId,
                dashboardId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_DASHBOARD_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getChart should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getChartUseCase.getChart(
                studyId,
                dashboardId,
                chartId
            )
        } returns DashboardTestUtil.getChartResponse()

        val res = dashboardRestController.getChart(
            studyId,
            dashboardId,
            chartId
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getChart should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getChart(
                studyId,
                dashboardId,
                chartId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getChart should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getChart(
                studyId,
                dashboardId,
                chartId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getChart should throw IllegalArgumentException if dashboardId is empty`(dashboardId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getChart(
                studyId,
                dashboardId,
                chartId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_DASHBOARD_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getChart should throw IllegalArgumentException if chartId is empty`(chartId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getChart(
                studyId,
                dashboardId,
                chartId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_CHART_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getChartList should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getChartUseCase.getChartList(
                studyId,
                dashboardId
            )
        } returns listOf(
            DashboardTestUtil.getChartResponse()
        )

        val res = dashboardRestController.getChartList(
            studyId,
            dashboardId
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getChartList should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getChartList(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getChartList should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getChartList(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getChartList should throw IllegalArgumentException if dashboardId is empty`(dashboardId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.getChartList(
                studyId,
                dashboardId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_DASHBOARD_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateChart should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getUpdateChartCommand()

        coEvery {
            updateChartUseCase.updateChart(
                studyId,
                dashboardId,
                chartId,
                command
            )
        } returns Unit

        val res = dashboardRestController.updateChart(
            studyId,
            dashboardId,
            chartId,
            command
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateChart should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getUpdateChartCommand()
        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.updateChart(
                studyId,
                dashboardId,
                chartId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateChart should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getUpdateChartCommand()
        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.updateChart(
                studyId,
                dashboardId,
                chartId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateChart should throw IllegalArgumentException if dashboardId is empty`(dashboardId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getUpdateChartCommand()
        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.updateChart(
                studyId,
                dashboardId,
                chartId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_DASHBOARD_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateChart should throw IllegalArgumentException if chartId is empty`(chartId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val command = DashboardTestUtil.getUpdateChartCommand()
        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.updateChart(
                studyId,
                dashboardId,
                chartId,
                command
            )
        }
        assertEquals(ExceptionMessage.EMPTY_CHART_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteChart should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            deleteChartUseCase.deleteChart(
                studyId,
                dashboardId,
                chartId
            )
        } returns Unit

        val res = dashboardRestController.deleteChart(
            studyId,
            dashboardId,
            chartId
        ).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteChart should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.deleteChart(
                studyId,
                dashboardId,
                chartId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteChart should throw IllegalArgumentException if dashboardId is empty`(dashboardId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.deleteChart(
                studyId,
                dashboardId,
                chartId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_DASHBOARD_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteChart should throw IllegalArgumentException if chartId is empty`(chartId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            dashboardRestController.deleteChart(
                studyId,
                dashboardId,
                chartId
            )
        }
        assertEquals(ExceptionMessage.EMPTY_CHART_ID, exception.message)
    }
}
