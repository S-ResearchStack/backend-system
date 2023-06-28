package com.samsung.healthcare.cloudstorageservice.adapter.storage

import com.samsung.healthcare.cloudstorageservice.POSITIVE_TEST
import com.samsung.healthcare.cloudstorageservice.application.config.AwsProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

internal class AwsStorageAdapterTest {
    private val awsProperties = AwsProperties("test-region", "test-access", "test-secret", "test-bucket")

    private val awsStorageAdapter = AwsStorageAdapter(awsProperties)

    private val signedUrlDuration = 120L

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUploadSignedUrl should work properly`() {
        val key = "1/in-lab-visit/u1234/test.txt"
        val url = awsStorageAdapter.getUploadSignedUrl(key, signedUrlDuration)
        println(url.host)
        println(url.query)
        Assertions.assertEquals(url.path, "/$key")
        Assertions.assertEquals(url.host, "${awsProperties.bucket}.s3.${awsProperties.region}.amazonaws.com")
        Assertions.assertTrue(
            url.query.contains("X-Amz-Expires=$signedUrlDuration") or
                url.query.contains("X-Amz-Expires=${signedUrlDuration - 1}")
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDownloadSignedUrl should work properly`() {
        val key = "1/in-lab-visit/u1234/test.txt"
        val url = awsStorageAdapter.getDownloadSignedUrl(key, signedUrlDuration)
        println(url.host)
        println(url.query)
        Assertions.assertEquals(url.path, "/$key")
        Assertions.assertEquals(url.host, "${awsProperties.bucket}.s3.${awsProperties.region}.amazonaws.com")
        Assertions.assertTrue(
            url.query.contains("X-Amz-Expires=$signedUrlDuration") or
                url.query.contains("X-Amz-Expires=${signedUrlDuration - 1}")
        )
    }
}
