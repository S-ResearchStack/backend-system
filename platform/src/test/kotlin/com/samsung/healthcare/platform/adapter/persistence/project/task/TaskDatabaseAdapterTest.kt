package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.TaskEntity
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.TaskStatus
import com.samsung.healthcare.platform.enums.TaskType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class TaskDatabaseAdapterTest {

    private val taskRepository = mockk<TaskRepository>()

    private val taskDatabaseAdapter = TaskDatabaseAdapter(taskRepository)

    private val task = Task(
        revisionId = RevisionId.from(1),
        id = "random-uuid",
        properties = emptyMap(),
        status = TaskStatus.PUBLISHED,
        type = TaskType.SURVEY
    )

    private val taskEntity = TaskEntity(
        revisionId = 1,
        id = "random-uuid",
        properties = emptyMap(),
        status = "PUBLISHED",
        type = "SURVEY",
        createdAt = null,
        publishedAt = null,
        outdatedAt = null,
        deletedAt = null,
    )

    private val invalidTaskEntity = TaskEntity(
        revisionId = 1,
        id = "random-uuid",
        properties = emptyMap(),
        status = "published",
        type = "survey",
        createdAt = null,
        publishedAt = null,
        outdatedAt = null,
        deletedAt = null,
    )

    private val updatedTask = Task(
        revisionId = RevisionId.from(2),
        id = "random-uuid",
        properties = emptyMap(),
        status = TaskStatus.COMPLETED,
        type = TaskType.SURVEY
    )

    private val updatedTaskEntity = TaskEntity(
        revisionId = 2,
        id = "random-uuid",
        properties = emptyMap(),
        status = "COMPLETED",
        type = "SURVEY",
        createdAt = null,
        publishedAt = null,
        outdatedAt = null,
        deletedAt = null,
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `findByPeriod should not emit event`() = runTest {

        coEvery {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndStatus(
                any(),
                any(),
                any(),
            )
        } returns flowOf(taskEntity)

        val startTime = LocalDateTime.now()
        val endTime = LocalDateTime.now()

        assertEquals(1, taskDatabaseAdapter.findByPeriod(startTime, endTime, "PUBLISHED", null).toList().size)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriod without status should throw Exception if repository has invalid status`() = runTest {

        coEvery {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
                any(),
                any()
            )
        } returns flowOf(invalidTaskEntity)

        val startTime = LocalDateTime.now()
        val endTime = LocalDateTime.now()

        assertThrows<IllegalArgumentException> {
            taskDatabaseAdapter.findByPeriod(startTime, endTime, null, null).toList()
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriod with status should throw Exception if repository has invalid status`() = runTest {

        coEvery {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndStatus(
                any(),
                any(),
                any()
            )
        } returns flowOf(invalidTaskEntity)

        val startTime = LocalDateTime.now()
        val endTime = LocalDateTime.now()

        assertThrows<IllegalArgumentException> {
            taskDatabaseAdapter.findByPeriod(startTime, endTime, "published", null).toList()
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriod with type should throw Exception if repository has invalid type`() = runTest {

        coEvery {
            taskRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndType(
                any(),
                any(),
                any()
            )
        } returns flowOf(invalidTaskEntity)

        val startTime = LocalDateTime.now()
        val endTime = LocalDateTime.now()

        assertThrows<IllegalArgumentException> {
            taskDatabaseAdapter.findByPeriod(startTime, endTime, null, "survey").toList()
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findByPublishedAt should not emit event`() = runTest {

        coEvery {
            taskRepository.findByPublishedAtGreaterThanEqualAndPublishedAtLessThanAndStatusEquals(
                any(),
                any()
            )
        } returns flowOf(taskEntity)

        val lastSyncTime = LocalDateTime.now()
        val endTime = LocalDateTime.now()

        assertEquals(1, taskDatabaseAdapter.findByPublishedAt(lastSyncTime, endTime).toList().size)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPublishedAt should throw IllegalArgumentException if repository has invalid status`() = runTest {

        coEvery {
            taskRepository.findByPublishedAtGreaterThanEqualAndPublishedAtLessThanAndStatusEquals(
                any(),
                any()
            )
        } returns flowOf(invalidTaskEntity)

        val lastSyncTime = LocalDateTime.now()
        val endTime = LocalDateTime.now()

        assertThrows<IllegalArgumentException> { taskDatabaseAdapter.findByPublishedAt(lastSyncTime, endTime).toList() }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `findById should not emit event`() = runTest {

        coEvery {
            taskRepository.findByIdIn(any())
        } returns flowOf(taskEntity)

        assertEquals(1, taskDatabaseAdapter.findById("id").toList().size)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `create task should not emit event`() = runTest {

        coEvery {
            taskRepository.save(any())
        } returns taskEntity

        val createdTask = taskDatabaseAdapter.create(task)

        assertEquals(task.id, createdTask.id)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `update task should not emit event`() = runTest {

        coEvery {
            taskRepository.findById(any())
        } returns taskEntity

        coEvery {
            taskRepository.save(any())
        } returns updatedTaskEntity

        val newTask = taskDatabaseAdapter.update(updatedTask)

        assertEquals(updatedTask.id, newTask.id)
        assertEquals(updatedTask.status, newTask.status)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update task should raise IllegalArgumentException if new revisionId is null`() = runTest {
        assertThrows<IllegalArgumentException> {
            taskDatabaseAdapter.update(
                Task(
                    revisionId = null,
                    id = "random-uuid",
                    properties = emptyMap(),
                    status = TaskStatus.COMPLETED,
                    type = TaskType.SURVEY
                )
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update task should raise IllegalArgumentException if task id is not found`() = runTest {
        coEvery {
            taskRepository.findById(any())
        } returns null

        assertThrows<IllegalArgumentException> { taskDatabaseAdapter.update(updatedTask) }
    }
}
