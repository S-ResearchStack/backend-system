package com.samsung.healthcare.platform.application.config

import com.samsung.healthcare.platform.application.config.ApplicationProperties.NewDatabaseConfig
import org.flywaydb.core.api.Location
import org.flywaydb.core.internal.scanner.LocationScannerCache
import org.flywaydb.core.internal.scanner.ResourceNameCache
import org.flywaydb.core.internal.scanner.classpath.ClassPathScanner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets

@Configuration
class ProjectConfig {

    @Bean
    fun newProjectConfig(applicationProperties: ApplicationProperties): NewProjectConfig =
        NewProjectConfig(
            applicationProperties.newDatabaseConfig,
            readSchemasFromResource()
        )

    private fun readSchemasFromResource() = ClassPathScanner(
        Nothing::class.java,
        javaClass.classLoader,
        StandardCharsets.UTF_8,
        Location("project_tables"),
        ResourceNameCache(),
        LocationScannerCache(),
        true
    ).scanForResources()
        .map { it.read().readLines().joinToString(" ") }

    data class NewProjectConfig(
        val newDatabaseConfig: NewDatabaseConfig,
        val schemas: List<String>
    )
}
