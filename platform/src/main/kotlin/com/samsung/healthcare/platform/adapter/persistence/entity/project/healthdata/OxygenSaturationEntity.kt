package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.OxygenSaturationMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.OxygenSaturation
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("oxygen_saturations")
data class OxygenSaturationEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val value: Double,
) : SampleDataEntity(id, userId, time)

fun OxygenSaturation.toEntity(userId: UserId): OxygenSaturationEntity =
    OxygenSaturationMapper.INSTANCE.toEntity(this, userId)
