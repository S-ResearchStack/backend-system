package com.samsung.healthcare.cloudstorageservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class CloudStorageServiceApplication

fun main(args: Array<String>) {
    runApplication<CloudStorageServiceApplication>(*args)
}
