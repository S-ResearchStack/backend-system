package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.SleepStageMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.SleepStage
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("sleepstage")
data class SleepStageEntity(
    override val id: Int? = null,
    override val userId: String,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val stage: String,
) : IntervalDataEntity(id, userId, startTime, endTime)

fun SleepStage.toEntity(userId: UserId): SleepStageEntity =
    SleepStageMapper.INSTANCE.toEntity(this, userId)
