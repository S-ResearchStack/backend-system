package com.samsung.healthcare.cloudstorageservice.adapter.storage

import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import com.google.cloud.storage.Storage.SignUrlOption
import com.google.cloud.storage.StorageOptions
import com.samsung.healthcare.cloudstorageservice.application.config.GcpProperties
import com.samsung.healthcare.cloudstorageservice.application.port.output.DeleteObjectPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.DownloadObjectPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.ListObjectsPort
import com.samsung.healthcare.cloudstorageservice.application.port.output.UploadObjectPort
import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.net.URL
import java.util.concurrent.TimeUnit

@ConditionalOnProperty(prefix = "app", name = ["storage-type"], havingValue = "GCP")
@Component
class GcpStorageAdapter(
    private val gcpProperties: GcpProperties,
) : UploadObjectPort, DownloadObjectPort, ListObjectsPort, DeleteObjectPort {
    override fun list(prefix: String): List<ObjectInfo> {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(gcpProperties.projectId).build().service
        val blobIterator = storage.list(gcpProperties.bucket, Storage.BlobListOption.prefix(prefix))
            .iterateAll().iterator()

        val objects = mutableListOf<ObjectInfo>()
        while (blobIterator.hasNext()) {
            val blob: Blob = blobIterator.next()
            objects.add(ObjectInfo(blob.name, blob.size))
        }

        return objects
    }

    override fun getUploadSignedUrl(objectName: String, urlDuration: Long): URL =
        getSignedUrl(objectName, HttpMethod.PUT, urlDuration)

    override fun getDownloadSignedUrl(objectName: String, urlDuration: Long): URL =
        getSignedUrl(objectName, HttpMethod.GET, urlDuration, SignUrlOption.withV2Signature())

    override fun getDeleteSignedUrl(objectName: String, urlDuration: Long): URL =
        getSignedUrl(objectName, HttpMethod.DELETE, urlDuration)

    fun getSignedUrl(
        objectName: String,
        httpMethod: HttpMethod,
        urlDuration: Long,
        signatureOption: SignUrlOption = SignUrlOption.withV4Signature(),
    ): URL {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(gcpProperties.projectId).build().service
        val blobId: BlobId = BlobId.of(gcpProperties.bucket, objectName)
        val blobInfo: BlobInfo = BlobInfo.newBuilder(blobId).build()

        return storage.signUrl(
            blobInfo,
            urlDuration,
            TimeUnit.SECONDS,
            SignUrlOption.httpMethod(httpMethod),
            signatureOption,
        )
    }
}
