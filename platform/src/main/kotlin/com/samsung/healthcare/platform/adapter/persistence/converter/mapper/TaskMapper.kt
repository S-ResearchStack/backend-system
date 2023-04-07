package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.TaskEntity
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import com.samsung.healthcare.platform.domain.project.task.Task
import com.samsung.healthcare.platform.enums.TaskStatus
import com.samsung.healthcare.platform.enums.TaskType
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

@Mapper
abstract class TaskMapper {
    companion object {
        val INSTANCE: TaskMapper = Mappers.getMapper(TaskMapper::class.java)
    }

    @Mapping(target = "revisionId", source = "revisionId.value")
    abstract fun toEntity(task: Task): TaskEntity

    @Mappings(
        Mapping(target = "revisionId", source = "."),
        Mapping(target = "status", source = "."),
        Mapping(target = "type", source = ".")
    )
    abstract fun toDomain(taskEntity: TaskEntity): Task

    fun mapRevisionId(value: TaskEntity): RevisionId =
        RevisionId.from(value.revisionId)

    fun mapStatus(value: TaskEntity): TaskStatus =
        TaskStatus.valueOf(value.status)

    fun mapType(value: TaskEntity): TaskType =
        TaskType.valueOf(value.type)
}
