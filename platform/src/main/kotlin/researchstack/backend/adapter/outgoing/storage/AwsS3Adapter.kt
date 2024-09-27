package researchstack.backend.adapter.outgoing.storage

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.storage.config.AwsProperties
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.application.port.outgoing.storage.UploadObjectPort
import researchstack.backend.application.port.outgoing.storage.UploadPresignedUrlResponse
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import software.amazon.awssdk.transfer.s3.S3TransferManager
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryDownload
import software.amazon.awssdk.transfer.s3.model.DownloadDirectoryRequest
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener
import java.net.URL
import java.nio.file.Paths
import java.time.Duration
import kotlin.math.min

@Component
class AwsS3Adapter(
    private val awsProperties: AwsProperties
) : UploadObjectPort, DownloadObjectPort {
    private val s3Presigner = S3Presigner.builder()
        .credentialsProvider(DefaultCredentialsProvider.builder().build())
        .region(Region.of(awsProperties.s3.region))
        .build()

    private val s3AsyncClient = S3AsyncClient.builder()
        .credentialsProvider(DefaultCredentialsProvider.builder().build())
        .region(Region.of(awsProperties.s3.region))
        .build()

    val transferManager: S3TransferManager = S3TransferManager.builder().s3Client(s3AsyncClient).build()

    override suspend fun getDownloadPresignedUrl(objectName: String): URL {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(awsProperties.s3.bucket)
            .key(objectName)
            .build()

        val getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(awsProperties.s3.presignedUrlDuration))
            .getObjectRequest(getObjectRequest)
            .build()

        return s3Presigner.presignGetObject(getObjectPresignRequest).url()
    }

    override suspend fun getUploadPresignedUrl(objectName: String): UploadPresignedUrlResponse {
        val putObjectRequestBuilder = PutObjectRequest.builder()
            .bucket(awsProperties.s3.bucket)
            .key(objectName)

        val headers = mutableMapOf<String, String>()

        val putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(awsProperties.s3.presignedUrlDuration))
            .putObjectRequest(putObjectRequestBuilder.build())
            .build()

        return UploadPresignedUrlResponse(
            s3Presigner.presignPutObject(putObjectPresignRequest).url(),
            headers
        )
    }

    override suspend fun getObject(objectName: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(awsProperties.s3.bucket)
            .key(objectName)
            .build()

        return s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
            .join().asUtf8String()
    }

    override suspend fun getPartialObject(objectName: String, range: String?): String? {
        val objectSize = getObjectSize(objectName)
        if (objectSize == 0L) return ""

        val length = min(objectSize, PARTIAL_OBJECT_DEFAULT_SIZE)

        val getObjectRequest = GetObjectRequest.builder()
            .bucket(awsProperties.s3.bucket)
            .key(objectName)
            .range(range ?: "bytes=0-$length")
            .build()

        return s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
            .join().asUtf8String()
    }

    override suspend fun listObjects(prefix: String): List<String> {
        return s3AsyncClient.listObjects(
            ListObjectsRequest.builder()
                .bucket(awsProperties.s3.bucket)
                .prefix(prefix)
                .build()
        )
            .join().contents().map { s3Object ->
                s3Object.key()
            }
    }

    override suspend fun listObjects(prefix: String, ext: String): List<String> {
        val listObjectRequest = ListObjectsRequest.builder()
            .bucket(awsProperties.s3.bucket)
            .prefix(prefix)
            .build()
        return s3AsyncClient.listObjects(listObjectRequest)
            .join()
            .contents()
            .filter { s3Object -> s3Object.key().endsWith(ext) }
            .map { s3Object -> s3Object.key() }
    }

    override suspend fun getObjectSize(objectName: String): Long {
        val request = HeadObjectRequest.builder()
            .bucket(awsProperties.s3.bucket)
            .key(objectName)
            .build()

        return s3AsyncClient.headObject(request)
            .get()
            .contentLength()
    }

    override suspend fun downloadDirectory(localDirectory: String, prefix: String): CompletedDirectoryDownload {
        return transferManager.downloadDirectory(
            DownloadDirectoryRequest.builder()
                .destination(Paths.get(localDirectory))
                .bucket(awsProperties.s3.bucket)
                .listObjectsV2RequestTransformer { request -> request.prefix(prefix) }
                .build()
        ).completionFuture().join()
    }

    override suspend fun uploadFile(path: String, objectName: String) {
        transferManager.uploadFile(
            UploadFileRequest.builder()
                .putObjectRequest { req -> req.bucket(awsProperties.s3.bucket).key(objectName) }
                .addTransferListener(LoggingTransferListener.create())
                .source(Paths.get(path))
                .build()
        )
            .completionFuture().join()
    }

    override suspend fun doesObjectExist(objectName: String): Boolean {
        return try {
            s3AsyncClient.headObject(
                HeadObjectRequest.builder()
                    .bucket(awsProperties.s3.bucket)
                    .key(objectName)
                    .build()
            ).join()
            true
        } catch (e: Exception) {
            logger.info("$objectName does not exist in bucket")
            false
        }
    }

    companion object {
        const val PARTIAL_OBJECT_DEFAULT_SIZE = 2048L

        private val logger = LoggerFactory.getLogger(AwsS3Adapter::class.java)
    }
}
