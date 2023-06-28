package com.samsung.healthcare.cloudstorageservice.application.port.output

import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo

interface ListObjectsPort {
    fun list(prefix: String): List<ObjectInfo>
}
