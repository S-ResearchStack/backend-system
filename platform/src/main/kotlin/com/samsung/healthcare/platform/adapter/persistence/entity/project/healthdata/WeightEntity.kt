package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.WeightMapper
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Weight
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("weights")
data class WeightEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val kilograms: Double,
) : SampleDataEntity(id, userId, time)

fun Weight.toEntity(userId: UserProfile.UserId): WeightEntity =
    WeightMapper.INSTANCE.toEntity(this, userId)
