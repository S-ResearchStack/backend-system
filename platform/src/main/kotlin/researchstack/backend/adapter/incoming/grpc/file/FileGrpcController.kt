package researchstack.backend.adapter.incoming.grpc.file

import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.file.UploadObjectUseCase
import researchstack.backend.grpc.FileServiceGrpcKt
import researchstack.backend.grpc.GetPresignedUrlRequest
import researchstack.backend.grpc.GetPresignedUrlResponse
import researchstack.backend.util.validateContext

@Component
class FileGrpcController(
    private val uploadObjectUseCase: UploadObjectUseCase
) : FileServiceGrpcKt.FileServiceCoroutineImplBase() {
    override suspend fun getPresignedUrl(request: GetPresignedUrlRequest): GetPresignedUrlResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(request.fileName, ExceptionMessage.EMPTY_FILEPATH)
        return GetPresignedUrlResponse.newBuilder().setPresignedUrl(
            uploadObjectUseCase.getUploadPresignedUrl(request.studyId, request.fileName).presignedUrl.toString()
        ).build()
    }
}
