package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.TaskResultEntity
import com.samsung.healthcare.platform.domain.project.task.TaskResult
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
abstract class TaskResultMapper {
    companion object {
        val INSTANCE: TaskResultMapper = Mappers.getMapper(TaskResultMapper::class.java)
    }

    @Mapping(target = "revisionId", source = "revisionId.value")
    abstract fun toEntity(taskResult: TaskResult): TaskResultEntity
}
