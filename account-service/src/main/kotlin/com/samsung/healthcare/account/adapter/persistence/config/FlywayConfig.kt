package com.samsung.healthcare.account.adapter.persistence.config

import com.samsung.healthcare.account.application.config.DatabaseProperties
import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfig(
    private val dbProperties: DatabaseProperties,
) {
    @Bean(initMethod = "migrate")
    fun flyway(): Flyway = Flyway(
        Flyway.configure().baselineOnMigrate(true).dataSource(
            "jdbc:${dbProperties.url}", dbProperties.user, dbProperties.password
        )
    )
}
