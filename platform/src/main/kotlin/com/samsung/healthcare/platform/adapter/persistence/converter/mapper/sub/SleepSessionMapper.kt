package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.SleepSessionEntity
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.SleepSession
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
abstract class SleepSessionMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(SleepSessionMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "sleepSession.id.value"),
        Mapping(target = "title", source = "sleepSession.title"),
        Mapping(target = "notes", source = "sleepSession.notes"),
        Mapping(target = "userId", source = "userId.value"),
        Mapping(target = "startTime", source = "sleepSession", qualifiedByName = ["startTime"]),
        Mapping(target = "endTime", source = "sleepSession", qualifiedByName = ["endTime"]),
    )
    abstract fun toEntity(sleepSession: SleepSession, userId: UserId): SleepSessionEntity

    @Named("endTime")
    fun mapEndTime(value: SleepSession): LocalDateTime =
        LocalDateTime.ofInstant(value.endTime, ZoneOffset.UTC)

    @Named("startTime")
    fun mapStartTime(value: SleepSession): LocalDateTime =
        LocalDateTime.ofInstant(value.startTime, ZoneOffset.UTC)
}
