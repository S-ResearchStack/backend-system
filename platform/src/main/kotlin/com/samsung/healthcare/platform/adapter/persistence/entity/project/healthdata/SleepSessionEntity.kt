package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.SleepSessionMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.SleepSession
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sleepsessions")
data class SleepSessionEntity(
    override val id: Int? = null,
    override val userId: String,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val title: String? = null,
    val notes: String? = null,
) : IntervalDataEntity(id, userId, startTime, endTime)

fun SleepSession.toEntity(userId: UserId): SleepSessionEntity =
    SleepSessionMapper.INSTANCE.toEntity(this, userId)
