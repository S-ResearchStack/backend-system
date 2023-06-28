package com.samsung.healthcare.cloudstorageservice.application.port.output

import java.net.URL

interface DeleteObjectPort {
    fun getDeleteSignedUrl(objectName: String, urlDuration: Long): URL
}
