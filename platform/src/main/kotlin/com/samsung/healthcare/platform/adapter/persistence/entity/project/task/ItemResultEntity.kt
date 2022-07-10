package com.samsung.healthcare.platform.adapter.persistence.entity.project.task

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.ItemResultMapper
import com.samsung.healthcare.platform.domain.project.task.ItemResult
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("item_results")
data class ItemResultEntity(
    @Id
    val id: Int?,
    val revisionId: Int,
    val taskId: String,
    val userId: String,
    val itemName: String,
    val result: String,
)

fun ItemResult.toEntity(): ItemResultEntity = ItemResultMapper.INSTANCE.toEntity(this)
