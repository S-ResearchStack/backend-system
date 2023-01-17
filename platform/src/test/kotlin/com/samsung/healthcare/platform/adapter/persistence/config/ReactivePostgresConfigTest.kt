package com.samsung.healthcare.platform.adapter.persistence.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.config.ApplicationProperties
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ReactivePostgresConfigTest {

    private val objectMapper = mockk<ObjectMapper>()

    private val applicationProperties = ApplicationProperties(
        ApplicationProperties.Db(
            "postgresql://research-hub.test.com/test",
            "host",
            80,
            "name",
            "schema",
            "user",
            "password"
        ),
        ApplicationProperties.NewDatabaseConfig("prefix", "postfix"),
        ApplicationProperties.JwksConfig("url"),
        ApplicationProperties.AccountService("url")
    )

    private val invalidApplicationProperties = ApplicationProperties(
        ApplicationProperties.Db("invalid_url", "host", 80, "", "schema", "user", "password"),
        ApplicationProperties.NewDatabaseConfig("prefix", "postfix"),
        ApplicationProperties.JwksConfig("url"),
        ApplicationProperties.AccountService("url")
    )

    private val reactivePostgresConfig = ReactivePostgresConfig(objectMapper, applicationProperties)
    private val invalidReactivePostgresConfig = ReactivePostgresConfig(objectMapper, invalidApplicationProperties)

    @Test
    @Tag(POSITIVE_TEST)
    fun `connectionFactory should not emit event`() {
        reactivePostgresConfig.connectionFactory()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `connectionFactoryOptionsBuilder should not emit event`() {
        reactivePostgresConfig.connectionFactoryOptionsBuilder()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `connectionFactoryOptionsBuilder raise error with invalid db url`() {
        assertThrows<IllegalArgumentException> { invalidReactivePostgresConfig.connectionFactoryOptionsBuilder() }
    }
}
