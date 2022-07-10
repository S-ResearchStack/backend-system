package com.samsung.healthcare.platform.application.port.output.project.task

import com.samsung.healthcare.platform.domain.project.task.Item
import kotlinx.coroutines.flow.Flow

interface ItemOutputPort {
    suspend fun findByRevisionIdAndTaskId(revisionId: Int, taskId: String): Flow<Item>

    suspend fun update(revisionId: Int, items: List<Item>)
}
