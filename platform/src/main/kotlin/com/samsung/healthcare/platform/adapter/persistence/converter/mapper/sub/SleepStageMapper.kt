package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.SleepStageEntity
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.SleepStage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
abstract class SleepStageMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(SleepStageMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "sleepStage.id.value"),
        Mapping(target = "stage", source = "sleepStage.stage"),
        Mapping(target = "userId", source = "userId.value"),
        Mapping(target = "startTime", source = "sleepStage", qualifiedByName = ["startTime"]),
        Mapping(target = "endTime", source = "sleepStage", qualifiedByName = ["endTime"]),
    )
    abstract fun toEntity(sleepStage: SleepStage, userId: UserId): SleepStageEntity

    @Named("endTime")
    fun mapEndTime(value: SleepStage): LocalDateTime =
        LocalDateTime.ofInstant(value.endTime, ZoneOffset.UTC)

    @Named("startTime")
    fun mapStartTime(value: SleepStage): LocalDateTime =
        LocalDateTime.ofInstant(value.startTime, ZoneOffset.UTC)
}
