package com.samsung.healthcare.trinoruleupdateservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
class TrinoRuleUpdateServiceApplication

fun main(args: Array<String>) {
    runApplication<TrinoRuleUpdateServiceApplication>(*args)
}
