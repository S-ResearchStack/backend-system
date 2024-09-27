package researchstack.backend.application.port.outgoing.storage

import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryDownload
import java.net.URL

interface DownloadObjectPort {
    suspend fun getDownloadPresignedUrl(objectName: String): URL

    suspend fun getObject(objectName: String): String

    suspend fun getPartialObject(objectName: String, range: String? = null): String?

    suspend fun listObjects(prefix: String): List<String>

    suspend fun listObjects(prefix: String, ext: String): List<String>

    suspend fun getObjectSize(objectName: String): Long

    suspend fun downloadDirectory(localDirectory: String, prefix: String): CompletedDirectoryDownload

    suspend fun doesObjectExist(objectName: String): Boolean
}
