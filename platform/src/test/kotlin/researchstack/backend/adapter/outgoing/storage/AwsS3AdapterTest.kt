package researchstack.backend.adapter.outgoing.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.blockhound.BlockHound
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.storage.config.AwsProperties

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AwsS3AdapterTest {
    private val awsProperties = AwsProperties(
        "",
        "",
        AwsProperties.S3(60, "test-region", "test-bucket")
    )

    private val awsS3Adapter = AwsS3Adapter(awsProperties)

    @BeforeAll()
    fun setAWSProperty() {
        System.setProperty("aws.accessKeyId", "test-access")
        System.setProperty("aws.secretAccessKey", "test-secret")
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `AWS API should work as non-blocking`() = runTest {
        BlockHound.install()
        val objectName = "study_id/file_name"

        withContext(Dispatchers.Default) {
            assertDoesNotThrow {
                awsS3Adapter.getDownloadPresignedUrl(objectName)
                awsS3Adapter.getUploadPresignedUrl(objectName)
            }
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDownloadPresignedUrl should work properly`() = runTest {
        val objectName = "study_id/file_name"
        val url = awsS3Adapter.getDownloadPresignedUrl(objectName)
        Assertions.assertEquals(url.path, "/$objectName")
        Assertions.assertEquals(url.host, "${awsProperties.s3.bucket}.s3.${awsProperties.s3.region}.amazonaws.com")
        Assertions.assertTrue(
            url.query.contains("X-Amz-Expires=${awsProperties.s3.presignedUrlDuration}") or
                url.query.contains("X-Amz-Expires=${awsProperties.s3.presignedUrlDuration - 1}")
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUploadPresignedUrl should work properly`() = runTest {
        val objectName = "study_id/file_name"
        val response = awsS3Adapter.getUploadPresignedUrl(objectName)
        Assertions.assertEquals(response.url.path, "/$objectName")
        Assertions.assertEquals(
            response.url.host,
            "${awsProperties.s3.bucket}.s3.${awsProperties.s3.region}.amazonaws.com"
        )
        Assertions.assertTrue(
            response.url.query.contains("X-Amz-Expires=${awsProperties.s3.presignedUrlDuration}") or
                response.url.query.contains("X-Amz-Expires=${awsProperties.s3.presignedUrlDuration - 1}")
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUploadPresignedUrl with tags should work properly`() = runTest {
        val objectName = "study_id/file_name"

        val publicResponse = awsS3Adapter.getUploadPresignedUrl(objectName)
        Assertions.assertEquals(publicResponse.url.path, "/$objectName")
        Assertions.assertEquals(
            publicResponse.url.host,
            "${awsProperties.s3.bucket}.s3.${awsProperties.s3.region}.amazonaws.com"
        )

        val privateResponse = awsS3Adapter.getUploadPresignedUrl(objectName)
        Assertions.assertEquals(privateResponse.url.path, "/$objectName")
        Assertions.assertEquals(
            privateResponse.url.host,
            "${awsProperties.s3.bucket}.s3.${awsProperties.s3.region}.amazonaws.com"
        )
    }
}
