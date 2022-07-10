package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.toEntity
import com.samsung.healthcare.platform.application.port.output.project.task.ItemResultOutputPort
import com.samsung.healthcare.platform.domain.project.task.ItemResult
import org.springframework.stereotype.Component

@Component
class ItemResultDatabaseAdapter(
    private val itemResultRepository: ItemResultRepository,
) : ItemResultOutputPort {
    override suspend fun create(itemResult: ItemResult) {
        itemResultRepository.save(itemResult.toEntity())
    }
}
