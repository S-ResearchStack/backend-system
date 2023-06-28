package com.samsung.healthcare.cloudstorageservice.application.port.output

import java.net.URL

interface UploadObjectPort {
    fun getUploadSignedUrl(objectName: String, urlDuration: Long): URL
}
