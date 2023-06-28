package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.domain.project.task.ItemResult
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class ItemResultDatabaseAdapterTest {

    private val itemResultRepository = mockk<ItemResultRepository>()

    private val itemResultDatabaseAdapter = ItemResultDatabaseAdapter(itemResultRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `creating itemResult should not emit event`() = runTest {
        val itemResult = ItemResult(
            id = null,
            revisionId = RevisionId.from(1),
            taskId = "task-uuid",
            userId = "user-uuid",
            itemName = "name",
            result = "result",
        )

        coEvery { itemResultRepository.save(any()) } returns itemResult.toEntity()
        assertEquals(Unit, itemResultDatabaseAdapter.create(itemResult))
    }
}
