package com.samsung.healthcare.platform.adapter.persistence.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.samsung.healthcare.platform.adapter.persistence.converter.JsonReadConverter
import com.samsung.healthcare.platform.adapter.persistence.converter.JsonWriteConverter
import com.samsung.healthcare.platform.application.config.ApplicationProperties
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories
@EnableTransactionManagement
class ReactivePostgresConfig(
    private val objectMapper: ObjectMapper,
    private val config: ApplicationProperties,
) : AbstractR2dbcConfiguration() {
    val multiTenantRoutingConnectionFactory = MultiTenantRoutingConnectionFactory(
        config,
        PostgresqlConnectionConfiguration.builder()
            .host(config.db.host)
            .port(config.db.port)
            .database(config.db.name)
            .schema(config.db.schema)
            .username(config.db.user)
            .password(config.db.password)
            .build()
    )

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        multiTenantRoutingConnectionFactory.setLenientFallback(false)
        multiTenantRoutingConnectionFactory.setDefaultTargetConnectionFactory(
            PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                    .host(config.db.host)
                    .port(config.db.port)
                    .database(config.db.name)
                    .schema(config.db.schema)
                    .username(config.db.user)
                    .password(config.db.password)
                    .build()
            )
        )
        multiTenantRoutingConnectionFactory.setTargetConnectionFactories(mutableMapOf<String, ConnectionFactory>())
        multiTenantRoutingConnectionFactory.loadConnections()
        multiTenantRoutingConnectionFactory.afterPropertiesSet()

        return multiTenantRoutingConnectionFactory
    }

    override fun getCustomConverters(): MutableList<Any> = mutableListOf(
        JsonReadConverter(objectMapper),
        JsonWriteConverter(objectMapper),
    )

    @Bean
    fun connectionFactoryOptionsBuilder(): ConnectionFactoryOptions.Builder =
        ConnectionFactoryOptions.parse("r2dbc:${config.db.url}")
            .mutate()
            .option(ConnectionFactoryOptions.USER, config.db.user)
            .option(ConnectionFactoryOptions.PASSWORD, config.db.password)
}
