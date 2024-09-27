package researchstack.backend.adapter.outgoing.storage.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("aws")
data class AwsProperties(
    val accessKeyId: String,
    val secretAccessKey: String,
    val s3: S3
) {
    data class S3(
        val presignedUrlDuration: Long,
        val region: String,
        val bucket: String
    )
}
