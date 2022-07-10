package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.BloodPressureEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.BloodPressure
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class BloodPressureMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(BloodPressureMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "bloodPressure.id.value"),
        Mapping(target = "time", source = "bloodPressure"),
        Mapping(target = "userId", source = "userId.value"),
    )
    abstract fun toEntity(bloodPressure: BloodPressure, userId: UserProfile.UserId): BloodPressureEntity

    fun mapTime(value: BloodPressure): LocalDateTime =
        LocalDateTime.ofInstant(value.time, ZoneOffset.UTC)
}
