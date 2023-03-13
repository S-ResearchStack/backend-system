package com.samsung.healthcare.platform.adapter.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.config.ReactivePostgresConfig
import com.samsung.healthcare.platform.adapter.persistence.entity.ProjectEntity
import com.samsung.healthcare.platform.application.config.ApplicationProperties
import com.samsung.healthcare.platform.application.config.ApplicationProperties.NewDatabaseConfig
import com.samsung.healthcare.platform.application.config.ProjectConfig.NewProjectConfig
import com.samsung.healthcare.platform.domain.Project.Companion.newProject
import io.mockk.coEvery
import io.mockk.mockk
import io.r2dbc.spi.ConnectionFactoryOptions.Builder
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.r2dbc.core.DatabaseClient

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class ProjectDatabaseAdapterTest {

    private val objectMapper = mockk<ObjectMapper>()

    private val projectRepository = mockk<ProjectRepository>()

    private val databaseClient = mockk<DatabaseClient>()

    private val builder = mockk<Builder>()

    private val newProjectConfig = NewProjectConfig(NewDatabaseConfig("prefix", "postfix"), emptyList())

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
        NewDatabaseConfig("prefix", "postfix"),
        ApplicationProperties.JwksConfig("url"),
        ApplicationProperties.AccountService("url")
    )

    private val reactivePostgresConfig = ReactivePostgresConfig(objectMapper, applicationProperties)

    private val projectDatabaseAdapter =
        ProjectDatabaseAdapter(projectRepository, databaseClient, builder, newProjectConfig, reactivePostgresConfig)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `creating project raise IllegalArgumentException if projectId is null`() = runTest {

        val project = newProject("name", emptyMap(), true)

        val projectEntity = ProjectEntity(null, "name", emptyMap(), true)

        coEvery { projectRepository.save(any()) } returns projectEntity

        assertThrows<IllegalArgumentException> { projectDatabaseAdapter.create(project) }
    }
}
