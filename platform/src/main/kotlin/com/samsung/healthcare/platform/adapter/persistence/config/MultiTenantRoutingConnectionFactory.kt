package com.samsung.healthcare.platform.adapter.persistence.config

import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.application.config.ApplicationProperties
import com.samsung.healthcare.platform.application.config.Constants
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Row
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.cast
import java.util.concurrent.ConcurrentHashMap

class MultiTenantRoutingConnectionFactory(
    private val config: ApplicationProperties,
    private val defaultConnectionConfig: PostgresqlConnectionConfiguration,
) : AbstractRoutingConnectionFactory() {
    private val resolvedConnectionFactories: ConcurrentHashMap<String, ConnectionFactory> = ConcurrentHashMap(
        mapOf(
            Constants.DEFAULT_TENANT_NAME to PostgresqlConnectionFactory(
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
    )

    override fun determineCurrentLookupKey(): Mono<Any> =
        ContextHolder.getProjectId()
            .cast()

    override fun determineTargetConnectionFactory(): Mono<ConnectionFactory> =
        determineCurrentLookupKey()
            .flatMap { key ->
                val connectionFactory = resolvedConnectionFactories[key]
                if (connectionFactory != null)
                    return@flatMap Mono.just(connectionFactory)
                registerProjectConnection(key)
            }
            .switchIfEmpty(super.determineTargetConnectionFactory())

    private fun registerProjectConnection(projectId: Any): Mono<ConnectionFactory> {
        return executeSql(
            "SELECT id FROM projects WHERE id = $projectId",
            PostgresqlConnectionFactory(defaultConnectionConfig)
        )
            .next()
            .map {
                registerProjectPostgresqlConnectionFactory(projectId.toString())
            }
    }

    fun loadConnections() {
        executeSql("SELECT id FROM projects", PostgresqlConnectionFactory(defaultConnectionConfig))
            .map { row ->
                val id = row.get("id")
                if (id.toString().toIntOrNull() != null) {
                    registerProjectPostgresqlConnectionFactory(id.toString())
                }
            }
            .subscribe()
    }

    private fun executeSql(sql: String, connectionFactory: PostgresqlConnectionFactory): Flux<Row> {
        return connectionFactory
            .create()
            .flatMapMany { conn ->
                conn.createStatement(sql).execute()
            }
            .flatMap {
                it.map { row, _ -> row }
            }
    }

    fun registerProjectPostgresqlConnectionFactory(projectId: String): ConnectionFactory {
        val connectionFactory = PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(config.db.host)
                .port(config.db.port)
                .database(config.db.name)
                .schema("${config.newDatabaseConfig.prefix}${projectId}${config.newDatabaseConfig.postfix}")
                .username(config.db.user)
                .password(config.db.password)
                .build()
        )
        resolvedConnectionFactories[projectId] = connectionFactory
        return connectionFactory
    }
}
