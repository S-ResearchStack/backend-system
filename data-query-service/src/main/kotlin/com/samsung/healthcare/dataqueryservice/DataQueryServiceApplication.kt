package com.samsung.healthcare.dataqueryservice

import com.netflix.graphql.dgs.webmvc.autoconfigure.DgsWebMvcAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        DgsWebMvcAutoConfiguration::class
    ]
)
@ConfigurationPropertiesScan
class DataQueryServiceApplication

fun main(args: Array<String>) {
    runApplication<DataQueryServiceApplication>(*args)
}
