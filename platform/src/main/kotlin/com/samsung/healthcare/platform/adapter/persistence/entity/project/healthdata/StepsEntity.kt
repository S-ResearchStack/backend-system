package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.StepsMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.Steps
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("steps")
data class StepsEntity(
    override val id: Int? = null,
    override val userId: String,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val count: Long,
) : IntervalDataEntity(id, userId, startTime, endTime)

fun Steps.toEntity(userId: UserId): StepsEntity =
    StepsMapper.INSTANCE.toEntity(this, userId)
