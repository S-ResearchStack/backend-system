package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.RespiratoryRateEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.RespiratoryRate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class RespiratoryRateMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(RespiratoryRateMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "respiratoryRate.id.value"),
        Mapping(target = "time", source = "respiratoryRate"),
        Mapping(target = "rpm", source = "respiratoryRate.rpm"),
        Mapping(target = "userId", source = "userId.value"),
    )
    abstract fun toEntity(respiratoryRate: RespiratoryRate, userId: UserProfile.UserId): RespiratoryRateEntity

    fun mapTime(value: RespiratoryRate): LocalDateTime =
        LocalDateTime.ofInstant(value.time, ZoneOffset.UTC)
}
