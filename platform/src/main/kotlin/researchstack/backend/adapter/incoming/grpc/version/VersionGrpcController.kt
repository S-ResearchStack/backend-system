package researchstack.backend.adapter.incoming.grpc.version

import com.google.protobuf.Empty
import org.springframework.stereotype.Component
import researchstack.backend.application.port.incoming.version.GetApplicationVersionUseCase
import researchstack.backend.grpc.VersionServiceGrpcKt
import researchstack.backend.grpc.VersionServiceOuterClass.GetApplicationVersionResponse

@Component
class VersionGrpcController(
    private val getApplicationVersionUseCase: GetApplicationVersionUseCase
) :
    VersionServiceGrpcKt.VersionServiceCoroutineImplBase() {
    override suspend fun getApplicationVersion(request: Empty): GetApplicationVersionResponse {
        val response = getApplicationVersionUseCase.getApplicationVersion()
        return GetApplicationVersionResponse.newBuilder()
            .setMinimum(response.minimum)
            .setLatest(response.latest)
            .build()
    }
}
