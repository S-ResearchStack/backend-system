package com.samsung.healthcare.account

import com.samsung.healthcare.account.application.config.InvitationProperties
import com.samsung.healthcare.account.application.config.JwkProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwkProperties::class, InvitationProperties::class)
class AccountServiceApplication

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<AccountServiceApplication>(*args)
}
