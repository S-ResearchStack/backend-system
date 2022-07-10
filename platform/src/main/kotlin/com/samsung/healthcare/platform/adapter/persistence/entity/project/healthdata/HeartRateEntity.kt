package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.HeartRateMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HeartRate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("heartrates")
data class HeartRateEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val bpm: Long,
) : SampleDataEntity(id, userId, time)

fun HeartRate.toEntity(userId: UserId): HeartRateEntity =
    HeartRateMapper.INSTANCE.toEntity(this, userId)
