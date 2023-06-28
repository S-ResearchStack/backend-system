package com.samsung.healthcare.cloudstorageservice.application.port.output

import java.net.URL

interface DownloadObjectPort {
    fun getDownloadSignedUrl(objectName: String, urlDuration: Long): URL
}
