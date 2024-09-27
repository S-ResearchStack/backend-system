package researchstack.backend.adapter.incoming.rest.file

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.file.GetFileUseCase
import researchstack.backend.application.port.incoming.file.GetZippedFileUseCase
import researchstack.backend.application.port.incoming.file.UploadObjectUseCase
import researchstack.backend.enums.StudyAssetType
import researchstack.backend.util.validateContext
import researchstack.backend.util.validateContextList
import researchstack.backend.util.validateEnum

@Component
class FileRestController(
    private val getFileUseCase: GetFileUseCase,
    private val uploadObjectUseCase: UploadObjectUseCase,
    private val getZippedFileUseCase: GetZippedFileUseCase
) {

    @Get("/studies/{studyId}/files/upload-url")
    suspend fun getUploadUrl(
        @Param studyId: String,
        @Param("filePath") filePath: String,
        @Param("assetType") assetType: String? = null
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContext(filePath, ExceptionMessage.EMPTY_FILEPATH)
        val uploadUrl = if (assetType != null) {
            validateEnum<StudyAssetType>(assetType, ExceptionMessage.INVALID_STUDY_ASSET_TYPE)
            uploadObjectUseCase.getUploadPresignedUrl(studyId, filePath, StudyAssetType.valueOf(assetType))
        } else {
            uploadObjectUseCase.getUploadPresignedUrl(studyId, filePath)
        }

        return HttpResponse.of(JsonHandler.toJson(uploadUrl))
    }

    @Post("/studies/{studyId}/files/download-urls")
    suspend fun getDownloadUrls(
        @Param studyId: String,
        @RequestObject filePaths: List<String>
        // assetType should be added later
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContextList(filePaths, ExceptionMessage.EMPTY_FILEPATH_LIST)
        val downloadUrlList = filePaths.map { getFileUseCase.getDownloadPresignedUrl(studyId, it) }
        return HttpResponse.of(JsonHandler.toJson(downloadUrlList))
    }

    @Post("/studies/{studyId}/zipped-files/download-urls")
    suspend fun getZippedFileDownloadUrls(
        @Param studyId: String,
        @RequestObject subjectNumbers: List<String>
    ): HttpResponse {
        validateContext(studyId, ExceptionMessage.EMPTY_STUDY_ID)
        validateContextList(subjectNumbers, ExceptionMessage.EMPTY_SUBJECT_NUMBER_LIST)
        val downloadUrlList = subjectNumbers.map { getZippedFileUseCase.getDownloadPresignedUrl(studyId, it) }
        return HttpResponse.of(JsonHandler.toJson(downloadUrlList))
    }
}
