package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.TotalCaloriesBurnedEntity
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.TotalCaloriesBurned
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.time.ZoneOffset

@Mapper
abstract class TotalCaloriesBurnedMapper {
    companion object {
        val INSTANCE = Mappers.getMapper(TotalCaloriesBurnedMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", source = "totalCaloriesBurned.id.value"),
        Mapping(target = "calories", source = "totalCaloriesBurned.calories"),
        Mapping(target = "userId", source = "userId.value"),
        Mapping(target = "startTime", source = "totalCaloriesBurned", qualifiedByName = ["startTime"]),
        Mapping(target = "endTime", source = "totalCaloriesBurned", qualifiedByName = ["endTime"]),
    )
    abstract fun toEntity(totalCaloriesBurned: TotalCaloriesBurned, userId: UserId): TotalCaloriesBurnedEntity

    @Named("endTime")
    fun mapEndTime(value: TotalCaloriesBurned): LocalDateTime =
        LocalDateTime.ofInstant(value.endTime, ZoneOffset.UTC)

    @Named("startTime")
    fun mapStartTime(value: TotalCaloriesBurned): LocalDateTime =
        LocalDateTime.ofInstant(value.startTime, ZoneOffset.UTC)
}
