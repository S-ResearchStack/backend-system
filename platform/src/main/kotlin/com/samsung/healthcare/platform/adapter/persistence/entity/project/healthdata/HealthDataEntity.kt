package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.BloodPressure
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType
import com.samsung.healthcare.platform.domain.project.healthdata.HeartRate
import com.samsung.healthcare.platform.domain.project.healthdata.SleepSession
import com.samsung.healthcare.platform.domain.project.healthdata.SleepStage
import com.samsung.healthcare.platform.domain.project.healthdata.Steps
import org.springframework.data.annotation.Id

abstract class HealthDataEntity(
    @Id
    open val id: Int? = null,
    open val userId: String,
)

fun HealthData.toEntity(userId: UserId): HealthDataEntity =
    when (this.type) {
        HealthDataType.BLOOD_PRESSURE -> (this as BloodPressure).toEntity(userId)
        HealthDataType.HEART_RATE -> (this as HeartRate).toEntity(userId)
        HealthDataType.SLEEP_SESSION -> (this as SleepSession).toEntity(userId)
        HealthDataType.SLEEP_STAGE -> (this as SleepStage).toEntity(userId)
        HealthDataType.STEPS -> (this as Steps).toEntity(userId)
    }
