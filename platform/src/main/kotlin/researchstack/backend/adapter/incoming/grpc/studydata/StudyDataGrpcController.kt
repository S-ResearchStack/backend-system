package researchstack.backend.adapter.incoming.grpc.studydata

import com.google.protobuf.Empty
import org.springframework.stereotype.Component
import researchstack.backend.application.port.incoming.studydata.AddStudyDataUseCase
import researchstack.backend.grpc.AddStudyDataFileRequest
import researchstack.backend.grpc.StudyDataServiceGrpcKt

@Component
class StudyDataGrpcController(
    private val addStudyDataUseCase: AddStudyDataUseCase
) : StudyDataServiceGrpcKt.StudyDataServiceCoroutineImplBase() {
    override suspend fun addStudyDataFile(request: AddStudyDataFileRequest): Empty {
        addStudyDataUseCase.addStudyDataFileInfo(
            studyId = request.studyId,
            filePath = request.filePath,
            fileName = request.fileName
        )
        return Empty.newBuilder().build()
    }
}
