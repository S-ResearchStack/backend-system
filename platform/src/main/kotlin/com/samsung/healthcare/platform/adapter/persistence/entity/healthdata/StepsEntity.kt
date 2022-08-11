package com.samsung.healthcare.platform.adapter.persistence.entity.healthdata

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.Steps
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneOffset

@Table("steps")
data class StepsEntity(
    override val id: Int? = null,
    override val userId: String,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val count: Long,
) : IntervalDataEntity(id, userId, startTime, endTime) {
    override fun toDomain(): Steps {
        TODO("Not yet implemented")
    }
}

fun Steps.toEntity(userId: UserId): StepsEntity =
    StepsEntity(
        userId = userId.value,
        startTime = LocalDateTime.ofInstant(this.startTime, ZoneOffset.UTC),
        endTime = LocalDateTime.ofInstant(this.endTime, ZoneOffset.UTC),
        count = this.count,
    )
