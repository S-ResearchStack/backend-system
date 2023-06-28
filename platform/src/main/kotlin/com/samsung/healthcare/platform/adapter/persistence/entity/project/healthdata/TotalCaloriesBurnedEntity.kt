package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.TotalCaloriesBurnedMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.TotalCaloriesBurned
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("total_calories_burned")
data class TotalCaloriesBurnedEntity(
    override val id: Int? = null,
    override val userId: String,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val calories: Double,
) : IntervalDataEntity(id, userId, startTime, endTime)

fun TotalCaloriesBurned.toEntity(userId: UserId): TotalCaloriesBurnedEntity =
    TotalCaloriesBurnedMapper.INSTANCE.toEntity(this, userId)
