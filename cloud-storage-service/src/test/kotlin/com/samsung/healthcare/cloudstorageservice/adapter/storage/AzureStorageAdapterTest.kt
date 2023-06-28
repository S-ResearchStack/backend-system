package com.samsung.healthcare.cloudstorageservice.adapter.storage

import com.samsung.healthcare.cloudstorageservice.POSITIVE_TEST
import com.samsung.healthcare.cloudstorageservice.application.config.AzureProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

internal class AzureStorageAdapterTest {
    private val azureProperties = AzureProperties("account", "key", "test-container")

    private val azureStorageAdapter = AzureStorageAdapter(azureProperties)

    private val signedUrlDuration = 120L

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUploadSignedUrl should work properly`() {
        val blobName = "test-name"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'Z&'")

        val url = azureStorageAdapter.getUploadSignedUrl(blobName, signedUrlDuration)
        val startDate = dateFormat.parse("""st=[0-9-%A-Za-z]+&""".toRegex().find(url.query)?.value?.removePrefix("st="))
        val endDate = dateFormat.parse("""se=[0-9-%A-Za-z]+&""".toRegex().find(url.query)?.value?.removePrefix("se="))

        Assertions.assertEquals("account.blob.core.windows.net", url.host)
        Assertions.assertEquals("/${azureProperties.containerName}/$blobName", url.path)
        Assertions.assertEquals(signedUrlDuration, TimeUnit.MILLISECONDS.toSeconds(endDate.time - startDate.time))
        Assertions.assertTrue(url.query.contains("sp=w"))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDownloadSignedUrl should work properly`() {
        val blobName = "test-name"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'Z&'")

        val url = azureStorageAdapter.getDownloadSignedUrl(blobName, signedUrlDuration)
        val startDate = dateFormat.parse("""st=[0-9-%A-Za-z]+&""".toRegex().find(url.query)?.value?.removePrefix("st="))
        val endDate = dateFormat.parse("""se=[0-9-%A-Za-z]+&""".toRegex().find(url.query)?.value?.removePrefix("se="))

        Assertions.assertEquals("account.blob.core.windows.net", url.host)
        Assertions.assertEquals("/${azureProperties.containerName}/$blobName", url.path)
        Assertions.assertEquals(signedUrlDuration, TimeUnit.MILLISECONDS.toSeconds(endDate.time - startDate.time))
        Assertions.assertTrue(url.query.contains("sp=r"))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getDeleteSignedUrl should work properly`() {
        val blobName = "test-name"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'Z&'")

        val url = azureStorageAdapter.getDeleteSignedUrl(blobName, signedUrlDuration)
        val startDate = dateFormat.parse("""st=[0-9-%A-Za-z]+&""".toRegex().find(url.query)?.value?.removePrefix("st="))
        val endDate = dateFormat.parse("""se=[0-9-%A-Za-z]+&""".toRegex().find(url.query)?.value?.removePrefix("se="))

        Assertions.assertEquals("account.blob.core.windows.net", url.host)
        Assertions.assertEquals("/${azureProperties.containerName}/$blobName", url.path)
        Assertions.assertEquals(signedUrlDuration, TimeUnit.MILLISECONDS.toSeconds(endDate.time - startDate.time))
        Assertions.assertTrue(url.query.contains("sp=d"))
    }
}
