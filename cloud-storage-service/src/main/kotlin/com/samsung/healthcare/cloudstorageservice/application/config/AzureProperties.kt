package com.samsung.healthcare.cloudstorageservice.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.azure")
data class AzureProperties(
    val accountName: String,
    val accountKey: String,
    val containerName: String,
)
