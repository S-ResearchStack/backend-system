package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.ItemEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ItemRepository : CoroutineCrudRepository<ItemEntity, Int> {
    suspend fun findByRevisionIdAndTaskId(revisionId: Int, taskId: String): Flow<ItemEntity>

    suspend fun deleteAllByRevisionId(revisionId: Int)
}
