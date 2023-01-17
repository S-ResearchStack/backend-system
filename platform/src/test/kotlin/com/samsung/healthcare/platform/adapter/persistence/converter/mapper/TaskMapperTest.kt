package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.TaskEntity
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.TaskStatus.DRAFT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

internal class TaskMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val task = Task(
            RevisionId.from(1),
            "2b3b286c-4000-454c-bd8e-875b123aa73c",
            emptyMap(),
            DRAFT,
            LocalDateTime.now()
        )

        val taskEntity = task.toEntity()

        assertThat(taskEntity.revisionId).isEqualTo(task.revisionId?.value)
        assertThat(taskEntity.id).isEqualTo(task.id)
        assertThat(taskEntity.properties).isEqualTo(task.properties)
        assertThat(taskEntity.status).isEqualTo(task.status.name)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert entity to domain`() {
        val taskEntity = TaskEntity(
            1,
            "2b3b286c-4000-454c-bd8e-875b123aa73c",
            emptyMap(),
            "DRAFT",
            LocalDateTime.now(),
            null,
            null,
            null
        )

        val task = taskEntity.toDomain()

        assertThat(task.revisionId?.value).isEqualTo(taskEntity.revisionId)
        assertThat(task.id).isEqualTo(taskEntity.id)
        assertThat(task.properties).isEqualTo(taskEntity.properties)
        assertThat(task.status.name).isEqualTo(taskEntity.status)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw IllegalArgumentException if revisionId is null`() {
        val taskEntity = TaskEntity(
            null,
            "2b3b286c-4000-454c-bd8e-875b123aa73c",
            emptyMap(),
            "DRAFT",
            null,
            null,
            null,
            null
        )

        assertThrows<IllegalArgumentException> { TaskMapper.INSTANCE.toDomain(taskEntity) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw IllegalArgumentException if status is invalid`() {
        val taskEntity = TaskEntity(
            1,
            "2b3b286c-4000-454c-bd8e-875b123aa73c",
            emptyMap(),
            "invalid",
            null,
            null,
            null,
            null
        )

        assertThrows<IllegalArgumentException> { TaskMapper.INSTANCE.toDomain(taskEntity) }
    }
}
