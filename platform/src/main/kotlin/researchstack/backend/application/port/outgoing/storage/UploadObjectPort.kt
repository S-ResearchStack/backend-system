package researchstack.backend.application.port.outgoing.storage

interface UploadObjectPort {
    suspend fun getUploadPresignedUrl(objectName: String): UploadPresignedUrlResponse

    suspend fun uploadFile(path: String, objectName: String)
}
