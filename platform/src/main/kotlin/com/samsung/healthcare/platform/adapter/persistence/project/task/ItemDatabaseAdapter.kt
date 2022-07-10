package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.application.port.output.project.task.ItemOutputPort
import com.samsung.healthcare.platform.domain.project.task.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class ItemDatabaseAdapter(
    val itemRepository: ItemRepository
) : ItemOutputPort {
    override suspend fun findByRevisionIdAndTaskId(revisionId: Int, taskId: String): Flow<Item> {
        return itemRepository.findByRevisionIdAndTaskId(revisionId, taskId).map { it.toDomain() }
    }

    override suspend fun update(revisionId: Int, items: List<Item>) {
        itemRepository.deleteAllByRevisionId(revisionId)

        items.forEach {
            itemRepository.save(it.toEntity())
        }
    }
}
