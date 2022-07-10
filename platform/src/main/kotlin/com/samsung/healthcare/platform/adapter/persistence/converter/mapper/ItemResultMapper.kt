package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.ItemResultEntity
import com.samsung.healthcare.platform.domain.project.task.ItemResult
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
abstract class ItemResultMapper {
    companion object {
        val INSTANCE: ItemResultMapper = Mappers.getMapper(ItemResultMapper::class.java)
    }

    @Mapping(target = "revisionId", source = "revisionId.value")
    abstract fun toEntity(itemResult: ItemResult): ItemResultEntity
}
