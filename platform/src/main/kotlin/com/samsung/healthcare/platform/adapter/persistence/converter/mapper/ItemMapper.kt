package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.ItemEntity
import com.samsung.healthcare.platform.domain.project.task.Item
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.enums.ItemType
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
abstract class ItemMapper {
    companion object {
        val INSTANCE: ItemMapper = Mappers.getMapper(ItemMapper::class.java)
    }

    @Mapping(target = "revisionId", source = "revisionId.value")
    abstract fun toEntity(item: Item): ItemEntity

    @Mapping(target = "revisionId", source = ".")
    @Mapping(target = "type", source = ".")
    abstract fun toDomain(itemEntity: ItemEntity): Item

    fun mapRevisionId(value: ItemEntity): RevisionId =
        RevisionId.from(value.revisionId)

    fun mapType(value: ItemEntity): ItemType =
        ItemType.valueOf(value.type)
}
