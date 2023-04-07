package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.OxygenSaturationEntity
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.OxygenSaturation
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class OxygenSaturationMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(OxygenSaturationMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "spO2.id.value"),
        Mapping(target = "time", source = "spO2"),
        Mapping(target = "value", source = "spO2.value"),
        Mapping(target = "userId", source = "userId.value")
    )
    abstract fun toEntity(spO2: OxygenSaturation, userId: UserId): OxygenSaturationEntity

    fun mapTime(value: OxygenSaturation): LocalDateTime =
        LocalDateTime.ofInstant(value.time, ZoneOffset.UTC)
}
