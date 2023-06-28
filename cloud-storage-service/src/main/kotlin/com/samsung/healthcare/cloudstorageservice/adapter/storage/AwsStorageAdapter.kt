package com.samsung.healthcare.cloudstorageservice.adapter.storage

import com.amazonaws.HttpMethod
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.samsung.healthcare.cloudstorageservice.application.config.AwsProperties
import com.samsung.healthcare.cloudstorageservice.application.port.output.DeleteObjectPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.DownloadObjectPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.ListObjectsPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.UploadObjectPort
import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration
import java.time.Instant
import java.util.Date

@ConditionalOnProperty(prefix = "app", name = ["storage-type"], havingValue = "AWS")
@Component
class AwsStorageAdapter(
    private val awsProperties: AwsProperties,
) : UploadObjectPort, DownloadObjectPort, ListObjectsPort, DeleteObjectPort {
    private val crd = AwsBasicCredentials.create(awsProperties.accessKeyId, awsProperties.secretAccessKey)
    private val s3AsyncClient = S3AsyncClient.builder()
        .credentialsProvider(
            AwsCredentialsProviderChain.builder().addCredentialsProvider { crd }.build()
        )
        .region(Region.of(awsProperties.region))
        .build()

    private val s3Presigner = S3Presigner.builder()
        .credentialsProvider(
            AwsCredentialsProviderChain.builder().addCredentialsProvider { crd }.build()
        )
        .region(Region.of(awsProperties.region))
        .build()

    override fun getUploadSignedUrl(key: String, urlDuration: Long): URL {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(awsProperties.bucket)
            .key(key)
            .build()

        val putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(urlDuration))
            .putObjectRequest(putObjectRequest)
            .build()

        return s3Presigner.presignPutObject(putObjectPresignRequest).url()
    }

    override fun getDownloadSignedUrl(key: String, urlDuration: Long): URL {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(awsProperties.bucket)
            .key(key)
            .build()

        val getObejctPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(urlDuration))
            .getObjectRequest(getObjectRequest)
            .build()

        return s3Presigner.presignGetObject(getObejctPresignRequest).url()
    }

    /**
     * NOTE AWS SDK for Java 2.x doesn't support the Delete Object Pre-Signed URL feature yet.
     * Related issue link: https://github.com/aws/aws-sdk-java-v2/issues/2520
     */
    override fun getDeleteSignedUrl(key: String, urlDuration: Long): URL {
        val s3Client = AmazonS3ClientBuilder.standard().withRegion(awsProperties.region).withCredentials(
            AWSCredentialsProviderChain(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(awsProperties.accessKeyId, awsProperties.secretAccessKey)
                )
            )
        ).build()

        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(awsProperties.bucket, key)
            .withMethod(HttpMethod.DELETE)
            .withExpiration(Date(Instant.now().toEpochMilli() + 1000 * urlDuration))

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest)
    }

    override fun list(prefix: String): List<ObjectInfo> {
        val future = s3AsyncClient.listObjects(
            ListObjectsRequest.builder()
                .bucket(awsProperties.bucket)
                .prefix(prefix)
                .build()
        )

        val objects = mutableListOf<ObjectInfo>()
        future.join().contents().forEach { obj ->
            objects.add(ObjectInfo(obj.key(), obj.size()))
        }

        return objects
    }
}
