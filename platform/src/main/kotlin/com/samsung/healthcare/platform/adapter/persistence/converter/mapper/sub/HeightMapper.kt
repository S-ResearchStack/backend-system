package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.HeightEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Height
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class HeightMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(HeightMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "height.id.value"),
        Mapping(target = "time", source = "height"),
        Mapping(target = "meters", source = "height.meters"),
        Mapping(target = "userId", source = "userId.value"),
    )
    abstract fun toEntity(height: Height, userId: UserProfile.UserId): HeightEntity

    fun mapTime(value: Height): LocalDateTime =
        LocalDateTime.ofInstant(value.time, ZoneOffset.UTC)
}
