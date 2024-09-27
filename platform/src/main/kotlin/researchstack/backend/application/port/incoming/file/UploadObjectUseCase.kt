package researchstack.backend.application.port.incoming.file

import researchstack.backend.enums.StudyAssetType

interface UploadObjectUseCase {
    suspend fun getUploadPresignedUrl(
        studyId: String,
        filePath: String,
        assetType: StudyAssetType? = null
    ): GetPresignedUrlResponse
}
