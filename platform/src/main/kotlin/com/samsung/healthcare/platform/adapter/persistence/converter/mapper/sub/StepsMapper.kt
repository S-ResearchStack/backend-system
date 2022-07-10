package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.StepsEntity
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.Steps
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class StepsMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(StepsMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "steps.id.value"),
        Mapping(target = "count", source = "steps.count"),
        Mapping(target = "userId", source = "userId.value"),
        Mapping(target = "startTime", source = "steps", qualifiedByName = ["startTime"]),
        Mapping(target = "endTime", source = "steps", qualifiedByName = ["endTime"]),
    )
    abstract fun toEntity(steps: Steps, userId: UserId): StepsEntity

    @Named("endTime")
    fun mapEndTime(value: Steps): LocalDateTime =
        LocalDateTime.ofInstant(value.endTime, ZoneOffset.UTC)

    @Named("startTime")
    fun mapStartTime(value: Steps): LocalDateTime =
        LocalDateTime.ofInstant(value.startTime, ZoneOffset.UTC)
}
