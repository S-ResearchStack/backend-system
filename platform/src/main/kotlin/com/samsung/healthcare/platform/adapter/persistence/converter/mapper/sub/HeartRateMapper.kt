package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.HeartRateEntity
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HeartRate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
abstract class HeartRateMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(HeartRateMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "heartRate.id.value"),
        Mapping(target = "time", source = "heartRate"),
        Mapping(target = "userId", source = "userId.value")
    )
    abstract fun toEntity(heartRate: HeartRate, userId: UserId): HeartRateEntity

    fun mapTime(value: HeartRate): LocalDateTime =
        LocalDateTime.ofInstant(value.time, ZoneOffset.UTC)
}
