package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.TaskResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class TaskResultDatabaseAdapterTest {
    private val taskResultRepository = mockk<TaskResultRepository>()

    private val taskResultDatabaseAdapter = TaskResultDatabaseAdapter(taskResultRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `creating taskResult should not emit event`() = runTest {
        val startTime = LocalDateTime.of(2022, 8, 7, 12, 0, 0)
        val submitTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)

        val taskResult = TaskResult(
            id = null,
            revisionId = RevisionId.from(1),
            taskId = "task-uuid",
            userId = "user-uuid",
            startedAt = startTime,
            submittedAt = submitTime,
        )

        coEvery { taskResultRepository.save(any()) } returns taskResult.toEntity()
        assertEquals(Unit, taskResultDatabaseAdapter.create(taskResult))
    }
}
