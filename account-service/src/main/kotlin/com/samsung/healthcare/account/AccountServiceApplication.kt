package com.samsung.healthcare.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class AccountServiceApplication

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<AccountServiceApplication>(*args)
}
