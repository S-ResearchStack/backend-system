package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.ItemEntity
import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.domain.project.task.Item
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.enums.ItemType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class ItemDatabaseAdapterTest {

    private val itemRepository = mockk<ItemRepository>()

    private val itemDatabaseAdapter = ItemDatabaseAdapter(itemRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByRevisionIdAndTaskId should raise error with wrong task type`() = runTest {

        coEvery { itemRepository.findByRevisionIdAndTaskId(any(), any()) } returns flowOf(
            ItemEntity(
                id = null,
                revisionId = 1,
                taskId = "uuid",
                name = "name",
                contents = emptyMap(),
                type = "WRONG_TYPE",
                sequence = 1,
            )
        )

        assertThrows<IllegalArgumentException> { itemDatabaseAdapter.findByRevisionIdAndTaskId(0, "taskId").toList() }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `update item should not emit event`() = runTest {
        val item = Item(
            id = null,
            revisionId = RevisionId.from(1),
            taskId = "uuid",
            name = "name",
            contents = emptyMap(),
            type = ItemType.ITEM,
            sequence = 1,
        )
        coEvery { itemRepository.save(any()) } returns item.toEntity()
        coEvery { itemRepository.deleteAllByRevisionId(any()) } returns Unit

        assertEquals(Unit, itemDatabaseAdapter.update(3, listOf(item)))
    }
}
