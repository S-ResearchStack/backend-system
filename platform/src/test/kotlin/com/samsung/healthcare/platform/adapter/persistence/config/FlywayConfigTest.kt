package com.samsung.healthcare.platform.adapter.persistence.config

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.application.config.ApplicationProperties
import io.mockk.every
import io.mockk.mockk
import org.flywaydb.core.api.FlywayException
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class FlywayConfigTest {

    private val flywayClassicConfig = mockk<ClassicConfiguration>()

    private val applicationProperties = mockk<ApplicationProperties>()

    private val flywayConfig = FlywayConfig(applicationProperties)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `flyway should raise FlywayException if connection fails`() {
        every { flywayClassicConfig.setDataSource(any(), any(), any()) } throws FlywayException("flyway exception")
        every { applicationProperties.db } returns ApplicationProperties.Db("", "", 80, "", "", "", "")

        assertThrows<FlywayException> { flywayConfig.flyway() }
    }
}
