package researchstack.backend.adapter.incoming.rest.data

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.data.ExecuteDataQueryCommand
import researchstack.backend.application.port.incoming.data.ExecuteDataQueryUseCase
import researchstack.backend.application.port.incoming.data.GetDataSourceUseCase
import researchstack.backend.util.validateContext

@Component
class DataRestController(
    private val getDataSourceUseCase: GetDataSourceUseCase,
    private val executeDataQueryUseCase: ExecuteDataQueryUseCase
) {

    @Get("/studies/{studyId}/databases")
    suspend fun getDatabaseNames(@Param("studyId") studyId: String): HttpResponse {
        val databases = getDataSourceUseCase.getDatabaseNames(studyId)
        return HttpResponse.of(JsonHandler.toJson(databases))
    }

    @Get("/studies/{studyId}/databases/{databaseName}/tables")
    suspend fun getTableNames(
        @Param("studyId") studyId: String,
        @Param("databaseName") databaseName: String
    ): HttpResponse {
        val tables = getDataSourceUseCase.getTableNames(databaseName)
        return HttpResponse.of(JsonHandler.toJson(tables))
    }

    @Post("/studies/{studyId}/databases/{databaseName}/query")
    suspend fun executeDataQuery(
        @Param("studyId") studyId: String,
        @Param("databaseName") databaseName: String,
        @RequestObject command: ExecuteDataQueryCommand
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(databaseName, ExceptionMessage.EMPTY_DATABASE_NAME)
        val result = executeDataQueryUseCase.execute(studyId, databaseName, command)
        return HttpResponse.of(JsonHandler.toJson(result))
    }
}
