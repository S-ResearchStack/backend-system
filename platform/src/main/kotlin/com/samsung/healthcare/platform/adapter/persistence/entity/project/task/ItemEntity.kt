package com.samsung.healthcare.platform.adapter.persistence.entity.project.task

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.ItemMapper
import com.samsung.healthcare.platform.domain.project.task.Item
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("items")
data class ItemEntity(
    @Id
    val id: Int?,
    val revisionId: Int,
    val taskId: String,
    val name: String,
    val contents: Map<String, Any>,
    val type: String,
    val sequence: Int
) {
    fun toDomain(): Item = ItemMapper.INSTANCE.toDomain(this)
}

fun Item.toEntity(): ItemEntity = ItemMapper.INSTANCE.toEntity(this)
