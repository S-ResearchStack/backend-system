package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.ProjectEntity
import com.samsung.healthcare.platform.adapter.persistence.entity.toEntity
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ProjectMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val project = Project(
            ProjectId.from(1),
            "project1",
            emptyMap(),
            true
        )

        val projectEntity = project.toEntity()

        assertThat(projectEntity.id).isEqualTo(project.id?.value)
        assertThat(projectEntity.name).isEqualTo(project.name)
        assertThat(projectEntity.isOpen).isEqualTo(project.isOpen)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert entity to domain`() {
        val projectEntity = ProjectEntity(
            1,
            "project1",
            emptyMap(),
            true
        )

        val project = projectEntity.toDomain()

        assertThat(project.id).isNotNull
        assertThat(project.id?.value).isEqualTo(projectEntity.id)
        assertThat(project.name).isEqualTo(projectEntity.name)
        assertThat(project.isOpen).isEqualTo(projectEntity.isOpen)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw IllegalArgumentException if id is null`() {
        val projectEntity = ProjectEntity(
            null,
            "project1",
            emptyMap(),
            true
        )

        assertThrows<IllegalArgumentException> { ProjectMapper.INSTANCE.toDomain(projectEntity) }
    }
}
