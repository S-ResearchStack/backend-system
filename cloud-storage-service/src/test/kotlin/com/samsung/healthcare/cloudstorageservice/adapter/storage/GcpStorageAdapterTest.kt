package com.samsung.healthcare.cloudstorageservice.adapter.storage

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.samsung.healthcare.cloudstorageservice.POSITIVE_TEST
import com.samsung.healthcare.cloudstorageservice.application.config.GcpProperties
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.net.URL
import java.util.concurrent.TimeUnit

internal class GcpStorageAdapterTest {
    private val storageBuilder = mockk<StorageOptions.Builder>()

    private val storage = mockk<Storage>()

    private val gcpProperties = GcpProperties("test-id", "test-bucket")

    private val gcpStorageAdapter = GcpStorageAdapter(gcpProperties)

    private val signedUrlDuration = 120L

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSignedUrl should work properly`() {
        mockkStatic(StorageOptions::class)
        every { StorageOptions.newBuilder().setProjectId(gcpProperties.projectId) } returns storageBuilder
        every { storageBuilder.build().service } returns storage
        every {
            storage.signUrl(any(), signedUrlDuration, TimeUnit.SECONDS, any(), any())
        } returns URL("https://test")

        Assertions.assertEquals(
            gcpStorageAdapter.getUploadSignedUrl("test-name", signedUrlDuration), URL("https://test")
        )
        Assertions.assertEquals(
            gcpStorageAdapter.getDownloadSignedUrl("test-name", signedUrlDuration), URL("https://test")
        )
        Assertions.assertEquals(
            gcpStorageAdapter.getDeleteSignedUrl("test-name", signedUrlDuration), URL("https://test")
        )
    }
}
