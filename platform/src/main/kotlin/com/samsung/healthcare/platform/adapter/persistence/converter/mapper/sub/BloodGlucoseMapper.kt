package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.BloodGlucoseEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.BloodGlucose
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class BloodGlucoseMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(BloodGlucoseMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "bloodGlucose.id.value"),
        Mapping(target = "time", source = "bloodGlucose"),
        Mapping(target = "millimolesPerLiter", source = "bloodGlucose.millimolesPerLiter"),
        Mapping(target = "userId", source = "userId.value"),
    )
    abstract fun toEntity(bloodGlucose: BloodGlucose, userId: UserProfile.UserId): BloodGlucoseEntity

    fun mapTime(value: BloodGlucose): LocalDateTime =
        LocalDateTime.ofInstant(value.time, ZoneOffset.UTC)
}
