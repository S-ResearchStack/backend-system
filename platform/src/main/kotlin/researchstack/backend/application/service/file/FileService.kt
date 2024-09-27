package researchstack.backend.application.service.file

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_FILE
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.file.GetFileUseCase
import researchstack.backend.application.port.incoming.file.GetPresignedUrlResponse
import researchstack.backend.application.port.incoming.file.UploadObjectUseCase
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.application.port.outgoing.storage.UploadObjectPort
import researchstack.backend.domain.common.Url
import researchstack.backend.enums.StudyAssetType

@Service
class FileService(
    private val uploadObjectPort: UploadObjectPort,
    private val downloadObjectPort: DownloadObjectPort
) : UploadObjectUseCase, GetFileUseCase {

    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_FILE])
    override suspend fun getUploadPresignedUrl(
        @Tenants studyId: String,
        filePath: String,
        assetType: StudyAssetType?
    ): GetPresignedUrlResponse {
        val objectName = getStudyPath(
            studyId = studyId,
            objectName = filePath,
            assetType = assetType
        )

        return uploadObjectPort.getUploadPresignedUrl(objectName).let {
            GetPresignedUrlResponse(it.url, it.headers)
        }
    }

    @Role(actions = [ACTION_READ], resources = [RESOURCE_FILE])
    override suspend fun getDownloadPresignedUrl(
        @Tenants studyId: String,
        filePath: String,
        assetType: StudyAssetType?
    ): Url {
        val objectName = getStudyPath(
            studyId = studyId,
            objectName = filePath,
            assetType = assetType
        )
        return Url(downloadObjectPort.getDownloadPresignedUrl(objectName).toString())
    }

    private fun getStudyPath(
        studyId: String,
        objectName: String,
        assetType: StudyAssetType?
    ): String {
        return if (assetType == null) {
            "$studyId/$objectName"
        } else {
            val baseDir = "$studyId/$STUDY_ASSET_DIR"
            when (assetType) {
                StudyAssetType.UNSPECIFIED -> "$baseDir/$objectName"
                StudyAssetType.IMAGE -> "$baseDir/$STUDY_ASSET_IMAGE_DIR/$objectName"
                StudyAssetType.DOCUMENT -> "$baseDir/$STUDY_ASSET_DOCUMENT_DIR/$objectName"
            }
        }
    }

    companion object {
        private const val STUDY_ASSET_DIR = "assets"
        private const val STUDY_ASSET_IMAGE_DIR = "images"
        private const val STUDY_ASSET_DOCUMENT_DIR = "documents"
    }
}
