package researchstack.backend.application.port.incoming.file

import researchstack.backend.domain.common.Url
import researchstack.backend.enums.StudyAssetType

interface GetFileUseCase {
    suspend fun getDownloadPresignedUrl(
        studyId: String,
        filePath: String,
        assetType: StudyAssetType? = null
    ): Url
}
