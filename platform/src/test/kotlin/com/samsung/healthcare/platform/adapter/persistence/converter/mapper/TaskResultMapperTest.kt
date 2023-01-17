package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.TaskResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime

internal class TaskResultMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val taskResult = TaskResult(
            1,
            RevisionId.from(1),
            "2b3b286c-4000-454c-bd8e-875b123aa73c",
            "jjyun.do",
            LocalDateTime.of(2022, 9, 7, 12, 0, 0),
            LocalDateTime.of(2022, 9, 7, 17, 0, 0)
        )

        val taskResultEntity = taskResult.toEntity()

        assertAll(
            "Task result mapping to entity",
            { assertEquals(taskResult.id, taskResultEntity.id) },
            { assertEquals(taskResult.revisionId.value, taskResultEntity.revisionId) },
            { assertEquals(taskResult.taskId, taskResultEntity.taskId) },
            { assertEquals(taskResult.userId, taskResultEntity.userId) },
            { assertEquals(taskResult.startedAt, taskResultEntity.startedAt) },
            { assertEquals(taskResult.submittedAt, taskResultEntity.submittedAt) }
        )
    }
}
