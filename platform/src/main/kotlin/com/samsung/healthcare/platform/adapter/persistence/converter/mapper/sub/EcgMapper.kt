package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.EcgEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Ecg
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class EcgMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(EcgMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "ecg.id.value"),
        Mapping(target = "userId", source = "userId.value")
    )
    abstract fun toEntity(ecg: Ecg, userId: UserProfile.UserId): EcgEntity

    fun mapTime(value: Instant): LocalDateTime =
        LocalDateTime.ofInstant(value, ZoneOffset.UTC)
}
