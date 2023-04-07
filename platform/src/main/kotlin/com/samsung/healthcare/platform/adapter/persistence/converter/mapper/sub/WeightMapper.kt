package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.WeightEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Weight
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class WeightMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(WeightMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "weight.id.value"),
        Mapping(target = "time", source = "weight"),
        Mapping(target = "userId", source = "userId.value"),
    )
    abstract fun toEntity(weight: Weight, userId: UserProfile.UserId): WeightEntity

    fun mapTime(value: Weight): LocalDateTime =
        LocalDateTime.ofInstant(value.time, ZoneOffset.UTC)
}
