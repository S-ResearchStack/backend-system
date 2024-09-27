package researchstack.backend.adapter.incoming.rest.healthdata

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.internal.common.kotlin.ArmeriaRequestCoroutineContext
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.healthdata.GetHealthDataUseCase
import researchstack.backend.application.port.incoming.healthdata.UploadBatchHealthDataCommand
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataCommand
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.util.validateContext

@Component
class HealthDataRestController(
    private val getHealthDataUseCase: GetHealthDataUseCase,
    private val uploadHealthDataUseCase: UploadHealthDataUseCase
) {

    @Post("/health-data")
    suspend fun syncHealthData(
        @Param("studyIds") studyIds: List<String>,
        @RequestObject uploadHealthDataCommand: UploadHealthDataCommand
    ): HttpResponse {
        require(studyIds.isNotEmpty()) { ExceptionMessage.EMPTY_STUDY_ID }
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)

        CoroutineScope(Dispatchers.IO).launch(
            ArmeriaRequestCoroutineContext(ServiceRequestContext.current())
        ) {
            uploadHealthDataUseCase.upload(
                Subject.SubjectId.from(userId),
                studyIds,
                uploadHealthDataCommand
            )
        }

        return HttpResponse.of(HttpStatus.OK)
    }

    @Post("/health-data/batch")
    suspend fun syncBatchHealthData(
        @Param("studyIds") studyIds: List<String>,
        @RequestObject uploadBatchHealthDataCommand: UploadBatchHealthDataCommand
    ): HttpResponse {
        require(studyIds.isNotEmpty()) { ExceptionMessage.EMPTY_STUDY_ID }
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)

        CoroutineScope(Dispatchers.IO).launch(
            ArmeriaRequestCoroutineContext(ServiceRequestContext.current())
        ) {
            uploadHealthDataUseCase.uploadBatch(
                Subject.SubjectId.from(userId),
                studyIds,
                uploadBatchHealthDataCommand
            )
        }

        return HttpResponse.of(HttpStatus.OK)
    }

    @Get("/health-data/types")
    suspend fun getStudyHealthDataList(): HttpResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val healthDataList = getHealthDataUseCase.getHealthDataList()
        return HttpResponse.of(JsonHandler.toJson(healthDataList))
    }
}
