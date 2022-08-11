package com.samsung.healthcare.platform.adapter.persistence.entity.healthdata

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.HealthData
import com.samsung.healthcare.platform.domain.healthdata.HeartRate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneOffset

@Table("heartrates")
data class HeartRateEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val bpm: Long,
) : SampleDataEntity(id, userId, time) {
    override fun toDomain(): HeartRate {
        requireNotNull(this.id)
        return HeartRate(
            HealthData.HealthDataId.from(this.id), time.toInstant(ZoneOffset.UTC), this.bpm
        )
    }
}

fun HeartRate.toEntity(userId: UserId): HeartRateEntity =
    HeartRateEntity(
        userId = userId.value,
        time = LocalDateTime.ofInstant(this.time, ZoneOffset.UTC),
        bpm = this.bpm,
    )
