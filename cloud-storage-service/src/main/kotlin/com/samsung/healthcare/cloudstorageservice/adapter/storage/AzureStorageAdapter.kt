package com.samsung.healthcare.cloudstorageservice.adapter.storage

import com.azure.storage.blob.BlobClientBuilder
import com.azure.storage.blob.BlobContainerClientBuilder
import com.azure.storage.blob.sas.BlobSasPermission
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues
import com.azure.storage.common.StorageSharedKeyCredential
import com.samsung.healthcare.cloudstorageservice.application.config.AzureProperties
import com.samsung.healthcare.cloudstorageservice.application.port.output.DeleteObjectPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.DownloadObjectPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.ListObjectsPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.UploadObjectPort
import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.net.URL
import java.time.OffsetDateTime

@ConditionalOnProperty(prefix = "app", name = ["storage-type"], havingValue = "AZURE")
@Component
class AzureStorageAdapter(
    private val azureProperties: AzureProperties,
) : UploadObjectPort, DownloadObjectPort, ListObjectsPort, DeleteObjectPort {
    private val endpoint =
        "https://${azureProperties.accountName}.blob.core.windows.net/${azureProperties.containerName}"
    private val credential = StorageSharedKeyCredential(azureProperties.accountName, azureProperties.accountKey)

    override fun getUploadSignedUrl(blobName: String, urlDuration: Long): URL {
        val blobClient = BlobClientBuilder()
            .endpoint(endpoint)
            .credential(credential)
            .blobName(blobName)
            .buildAsyncClient()

        val expiryTime = OffsetDateTime.now().plusSeconds(urlDuration)
        val blobSasPermission = BlobSasPermission().setWritePermission(true)
        val values = BlobServiceSasSignatureValues(expiryTime, blobSasPermission)
            .setStartTime(OffsetDateTime.now())
        return URL("${blobClient.blobUrl}?${blobClient.generateSas(values)}")
    }

    override fun getDownloadSignedUrl(blobName: String, urlDuration: Long): URL {
        val blobClient = BlobClientBuilder()
            .endpoint(endpoint)
            .credential(credential)
            .blobName(blobName)
            .buildAsyncClient()

        val expiryTime = OffsetDateTime.now().plusSeconds(urlDuration)
        val blobSasPermission = BlobSasPermission().setReadPermission(true)
        val values = BlobServiceSasSignatureValues(expiryTime, blobSasPermission)
            .setStartTime(OffsetDateTime.now())
        return URL("${blobClient.blobUrl}?${blobClient.generateSas(values)}")
    }

    override fun getDeleteSignedUrl(blobName: String, urlDuration: Long): URL {
        val blobClient = BlobClientBuilder()
            .endpoint(endpoint)
            .credential(credential)
            .blobName(blobName)
            .buildAsyncClient()

        val expiryTime = OffsetDateTime.now().plusSeconds(urlDuration)
        val blobSasPermission = BlobSasPermission().setDeletePermission(true)
        val values = BlobServiceSasSignatureValues(expiryTime, blobSasPermission)
            .setStartTime(OffsetDateTime.now())
        return URL("${blobClient.blobUrl}?${blobClient.generateSas(values)}")
    }

    override fun list(prefix: String): List<ObjectInfo> {
        val blobContainerClient = BlobContainerClientBuilder()
            .endpoint(endpoint)
            .credential(credential)
            .buildAsyncClient()

        val objects = mutableListOf<ObjectInfo>()
        blobContainerClient.listBlobsByHierarchy(prefix)
            .map { obj ->
                if (!obj.isPrefix) {
                    objects.add(ObjectInfo(obj.name, obj.properties.contentLength))
                }
            }
            .subscribe()

        return objects
    }
}
