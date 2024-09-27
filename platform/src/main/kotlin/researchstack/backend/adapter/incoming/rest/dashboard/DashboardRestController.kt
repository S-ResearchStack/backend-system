package researchstack.backend.adapter.incoming.rest.dashboard

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Decorator
import com.linecorp.armeria.server.annotation.Delete
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Patch
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.decorator.TenantHandlerHttpFunction
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.dashboard.CreateChartCommand
import researchstack.backend.application.port.incoming.dashboard.CreateChartUseCase
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardCommand
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardUseCase
import researchstack.backend.application.port.incoming.dashboard.DeleteChartUseCase
import researchstack.backend.application.port.incoming.dashboard.DeleteDashboardUseCase
import researchstack.backend.application.port.incoming.dashboard.GetChartUseCase
import researchstack.backend.application.port.incoming.dashboard.GetDashboardUseCase
import researchstack.backend.application.port.incoming.dashboard.UpdateChartCommand
import researchstack.backend.application.port.incoming.dashboard.UpdateChartUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.util.validateContext

@Decorator(TenantHandlerHttpFunction::class)
@Component
class DashboardRestController(
    private val createDashboardUseCase: CreateDashboardUseCase,
    private val getDashboardUseCase: GetDashboardUseCase,
    private val deleteDashboardUseCase: DeleteDashboardUseCase,
    private val createChartUseCase: CreateChartUseCase,
    private val updateChartUseCase: UpdateChartUseCase,
    private val getChartUseCase: GetChartUseCase,
    private val deleteChartUseCase: DeleteChartUseCase
) {

    @Post("/studies/{studyId}/dashboards")
    suspend fun createDashboard(
        @Param("studyId") studyId: String,
        @RequestObject command: CreateDashboardCommand
    ): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val dashboardId = createDashboardUseCase.createDashboard(studyId, command)
        return HttpResponse.of(JsonHandler.toJson(dashboardId))
    }

    @Get("/studies/{studyId}/dashboards/{dashboardId}")
    suspend fun getDashboard(
        @Param("studyId") studyId: String,
        @Param("dashboardId") dashboardId: String
    ): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(dashboardId, ExceptionMessage.EMPTY_DASHBOARD_ID)
        val dashboard = getDashboardUseCase.getDashboard(studyId, dashboardId)
        return HttpResponse.of(JsonHandler.toJson(dashboard))
    }

    @Get("/studies/{studyId}/dashboards")
    suspend fun getDashboardList(@Param("studyId") studyId: String): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val dashboardList = getDashboardUseCase.getDashboardList(studyId)
        return HttpResponse.of(JsonHandler.toJson(dashboardList))
    }

    @Delete("/studies/{studyId}/dashboards/{dashboardId}")
    suspend fun deleteDashboard(
        @Param("studyId") studyId: String,
        @Param("dashboardId") dashboardId: String
    ): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(dashboardId, ExceptionMessage.EMPTY_DASHBOARD_ID)
        deleteDashboardUseCase.deleteDashboard(studyId, dashboardId)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Post("/studies/{studyId}/dashboards/{dashboardId}/charts")
    suspend fun createChart(
        @Param("studyId") studyId: String,
        @Param("dashboardId") dashboardId: String,
        @RequestObject command: CreateChartCommand
    ): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(dashboardId, ExceptionMessage.EMPTY_DASHBOARD_ID)
        val chartId = createChartUseCase.createChart(studyId, dashboardId, command)
        return HttpResponse.of(JsonHandler.toJson(chartId))
    }

    @Get("/studies/{studyId}/dashboards/{dashboardId}/charts/{chartId}")
    suspend fun getChart(
        @Param("studyId") studyId: String,
        @Param("dashboardId") dashboardId: String,
        @Param("chartId") chartId: String
    ): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(dashboardId, ExceptionMessage.EMPTY_DASHBOARD_ID)
        validateContext(chartId, ExceptionMessage.EMPTY_CHART_ID)
        val chart = getChartUseCase.getChart(studyId, dashboardId, chartId)
        return HttpResponse.of(JsonHandler.toJson(chart))
    }

    @Get("/studies/{studyId}/dashboards/{dashboardId}/charts")
    suspend fun getChartList(
        @Param("studyId") studyId: String,
        @Param("dashboardId") dashboardId: String
    ): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(dashboardId, ExceptionMessage.EMPTY_DASHBOARD_ID)
        val chartList = getChartUseCase.getChartList(studyId, dashboardId)
        return HttpResponse.of(JsonHandler.toJson(chartList))
    }

    @Patch("/studies/{studyId}/dashboards/{dashboardId}/charts/{chartId}")
    suspend fun updateChart(
        @Param("studyId") studyId: String,
        @Param("dashboardId") dashboardId: String,
        @Param("chartId") chartId: String,
        @RequestObject command: UpdateChartCommand
    ): HttpResponse {
        validateContext(
            ServiceRequestContext.current().getUserId(),
            ExceptionMessage.EMPTY_USER_ID
        )
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(dashboardId, ExceptionMessage.EMPTY_DASHBOARD_ID)
        validateContext(chartId, ExceptionMessage.EMPTY_CHART_ID)
        updateChartUseCase.updateChart(studyId, dashboardId, chartId, command)
        return HttpResponse.of(HttpStatus.OK)
    }

    @Delete("/studies/{studyId}/dashboards/{dashboardId}/charts/{chartId}")
    suspend fun deleteChart(
        @Param("studyId") studyId: String,
        @Param("dashboardId") dashboardId: String,
        @Param("chartId") chartId: String
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(dashboardId, ExceptionMessage.EMPTY_DASHBOARD_ID)
        validateContext(chartId, ExceptionMessage.EMPTY_CHART_ID)
        deleteChartUseCase.deleteChart(studyId, dashboardId, chartId)
        return HttpResponse.of(HttpStatus.OK)
    }
}
