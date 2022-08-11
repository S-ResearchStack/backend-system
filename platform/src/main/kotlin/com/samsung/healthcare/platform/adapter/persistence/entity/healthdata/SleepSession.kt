package com.samsung.healthcare.platform.adapter.persistence.entity.healthdata

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.SleepSession
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneId

@Table("sleepsessions")
data class SleepSessionEntity(
    override val id: Int? = null,
    override val userId: String,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val title: String? = null,
    val notes: String? = null,
) : IntervalDataEntity(id, userId, startTime, endTime) {
    override fun toDomain(): SleepSession {
        TODO("Not yet implemented")
    }
}

fun SleepSession.toEntity(userId: UserId): SleepSessionEntity =
    SleepSessionEntity(
        userId = userId.value,
        startTime = LocalDateTime.ofInstant(this.startTime, ZoneId.systemDefault()),
        endTime = LocalDateTime.ofInstant(this.endTime, ZoneId.systemDefault()),
        title = this.title,
        notes = this.notes,
    )
