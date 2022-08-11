package com.samsung.healthcare.platform

import com.samsung.healthcare.platform.application.config.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class HealthResearchPlatformApplication

fun main(args: Array<String>) {
    runApplication<HealthResearchPlatformApplication>(*args)
}
