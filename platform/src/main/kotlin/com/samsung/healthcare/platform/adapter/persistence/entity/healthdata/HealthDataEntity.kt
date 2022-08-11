package com.samsung.healthcare.platform.adapter.persistence.entity.healthdata

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.HealthData
import com.samsung.healthcare.platform.domain.healthdata.HealthData.HealthDataType
import com.samsung.healthcare.platform.domain.healthdata.HeartRate
import com.samsung.healthcare.platform.domain.healthdata.SleepSession
import com.samsung.healthcare.platform.domain.healthdata.SleepStage
import com.samsung.healthcare.platform.domain.healthdata.Steps
import org.springframework.data.annotation.Id

abstract class HealthDataEntity(
    @Id
    open val id: Int? = null,
    open val userId: String,
) {
    abstract fun toDomain(): HealthData
}

fun HealthData.toEntity(userId: UserId): HealthDataEntity =
    when (this.type) {
        HealthDataType.HEART_RATE -> (this as HeartRate).toEntity(userId)
        HealthDataType.SLEEP_SESSION -> (this as SleepSession).toEntity(userId)
        HealthDataType.SLEEP_STAGE -> (this as SleepStage).toEntity(userId)
        HealthDataType.STEPS -> (this as Steps).toEntity(userId)
    }
