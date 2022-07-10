package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.ProjectEntity
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

@Mapper
abstract class ProjectMapper {
    companion object {
        val INSTANCE: ProjectMapper = Mappers.getMapper(ProjectMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "id.value"),
        Mapping(target = "isOpen", source = ".")
    )
    abstract fun toEntity(project: Project): ProjectEntity

    @Mappings(
        Mapping(target = "id", source = "."),
        Mapping(target = "isOpen", source = ".")
    )
    abstract fun toDomain(projectEntity: ProjectEntity): Project

    fun mapId(value: ProjectEntity): ProjectId =
        ProjectId.from(value.id)

    fun mapIsOpen(value: ProjectEntity): Boolean =
        value.isOpen

    fun mapIsOpen(value: Project): Boolean =
        value.isOpen
}
