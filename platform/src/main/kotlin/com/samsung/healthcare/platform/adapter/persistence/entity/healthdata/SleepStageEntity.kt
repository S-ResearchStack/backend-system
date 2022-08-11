package com.samsung.healthcare.platform.adapter.persistence.entity.healthdata

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.SleepStage
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneOffset

@Table("sleepstage")
data class SleepStageEntity(
    override val id: Int? = null,
    override val userId: String,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val stage: String,
) : IntervalDataEntity(id, userId, startTime, endTime) {
    override fun toDomain(): SleepStage {
        TODO("Not yet implemented")
    }
}

fun SleepStage.toEntity(userId: UserId): SleepStageEntity =
    SleepStageEntity(
        userId = userId.value,
        startTime = LocalDateTime.ofInstant(this.startTime, ZoneOffset.UTC),
        endTime = LocalDateTime.ofInstant(this.endTime, ZoneOffset.UTC),
        stage = this.stage,
    )
