package researchstack.backend.application.port.incoming.file

import researchstack.backend.domain.common.Url

interface GetZippedFileUseCase {
    suspend fun getDownloadPresignedUrl(
        studyId: String,
        subjectNumber: String? = null
    ): Url
}
