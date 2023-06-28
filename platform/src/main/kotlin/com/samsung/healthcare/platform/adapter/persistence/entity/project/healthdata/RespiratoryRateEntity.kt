package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.RespiratoryRateMapper
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.RespiratoryRate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("respiratory_rates")
data class RespiratoryRateEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val rpm: Double,
) : SampleDataEntity(id, userId, time)

fun RespiratoryRate.toEntity(userId: UserProfile.UserId): RespiratoryRateEntity =
    RespiratoryRateMapper.INSTANCE.toEntity(this, userId)
