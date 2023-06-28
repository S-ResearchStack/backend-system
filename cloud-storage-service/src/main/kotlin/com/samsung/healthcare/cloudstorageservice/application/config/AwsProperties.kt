package com.samsung.healthcare.cloudstorageservice.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.aws")
data class AwsProperties(
    val region: String,
    val accessKeyId: String,
    val secretAccessKey: String,
    val bucket: String,
)
