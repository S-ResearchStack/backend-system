package com.samsung.healthcare.cloudstorageservice.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.gcp")
data class GcpProperties(
    val projectId: String,
    val bucket: String,
)
