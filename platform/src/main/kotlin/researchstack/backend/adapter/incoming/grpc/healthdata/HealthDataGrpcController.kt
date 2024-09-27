package researchstack.backend.adapter.incoming.grpc.healthdata

import com.google.protobuf.Empty
import com.linecorp.armeria.internal.common.kotlin.ArmeriaRequestCoroutineContext
import com.linecorp.armeria.server.ServiceRequestContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.mapper.healthdata.toDomain
import researchstack.backend.adapter.incoming.mapper.toDomain
import researchstack.backend.application.port.incoming.healthdata.UploadBatchHealthDataCommand
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataCommand
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.grpc.BatchHealthDataSyncRequest
import researchstack.backend.grpc.HealthDataServiceGrpcKt
import researchstack.backend.grpc.HealthDataSyncRequest
import researchstack.backend.util.validateContext

@Component
class HealthDataGrpcController(
    private val uploadHealthDataUseCase: UploadHealthDataUseCase
) : HealthDataServiceGrpcKt.HealthDataServiceCoroutineImplBase() {
    override suspend fun syncHealthData(request: HealthDataSyncRequest): Empty {
        val studyIds = request.studyIdsList
        require(studyIds.isNotEmpty()) { ExceptionMessage.EMPTY_STUDY_ID }
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)

        CoroutineScope(Dispatchers.IO).launch(
            ArmeriaRequestCoroutineContext(ServiceRequestContext.current())
        ) {
            uploadHealthDataUseCase.upload(
                Subject.SubjectId.from(userId),
                studyIds,
                UploadHealthDataCommand(
                    request.healthData.type.toDomain(),
                    request.healthData.dataList.map { it.toDomain() }
                )
            )
        }

        return Empty.newBuilder().build()
    }

    override suspend fun syncBatchHealthData(request: BatchHealthDataSyncRequest): Empty {
        val studyIds = request.studyIdsList
        require(studyIds.isNotEmpty()) { ExceptionMessage.EMPTY_STUDY_ID }
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)

        CoroutineScope(Dispatchers.IO).launch(
            ArmeriaRequestCoroutineContext(ServiceRequestContext.current())
        ) {
            uploadHealthDataUseCase.uploadBatch(
                Subject.SubjectId.from(userId),
                studyIds,
                UploadBatchHealthDataCommand(
                    request.healthDataList.map {
                        UploadBatchHealthDataCommand.BatchHealthData(
                            it.type.toDomain(),
                            it.dataList.map { it.toDomain() }
                        )
                    }
                )
            )
        }

        return Empty.newBuilder().build()
    }
}
