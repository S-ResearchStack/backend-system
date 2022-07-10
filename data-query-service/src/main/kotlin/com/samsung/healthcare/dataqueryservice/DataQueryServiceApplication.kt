package com.samsung.healthcare.dataqueryservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DataQueryServiceApplication

fun main(args: Array<String>) {
    runApplication<DataQueryServiceApplication>(*args)
}
