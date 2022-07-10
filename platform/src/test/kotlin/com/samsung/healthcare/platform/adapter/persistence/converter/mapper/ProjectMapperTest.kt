package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.ProjectEntity
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ProjectMapperTest {
    @Test
    fun `should convert domain to entity`() {
        val project = Project(
            ProjectId.from(1),
            "project1",
            emptyMap(),
            true
        )

        val projectEntity = ProjectMapper.INSTANCE.toEntity(project)

        assertThat(projectEntity.id).isEqualTo(project.id?.value)
        assertThat(projectEntity.name).isEqualTo(project.name)
        assertThat(projectEntity.isOpen).isEqualTo(project.isOpen)
    }

    @Test
    fun `should convert entity to domain`() {
        val projectEntity = ProjectEntity(
            1,
            "project1",
            emptyMap(),
            true
        )

        val project = ProjectMapper.INSTANCE.toDomain(projectEntity)

        assertThat(project.id).isNotNull
        assertThat(project.id?.value).isEqualTo(projectEntity.id)
        assertThat(project.name).isEqualTo(projectEntity.name)
        assertThat(project.isOpen).isEqualTo(projectEntity.isOpen)
    }
}
