package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.BloodPressure
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.BLOOD_PRESSURE
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.HEART_RATE
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.OXYGEN_SATURATION
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.SLEEP_SESSION
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.SLEEP_STAGE
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.STEPS
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType.WEIGHT
import com.samsung.healthcare.platform.domain.project.healthdata.HeartRate
import com.samsung.healthcare.platform.domain.project.healthdata.OxygenSaturation
import com.samsung.healthcare.platform.domain.project.healthdata.SleepSession
import com.samsung.healthcare.platform.domain.project.healthdata.SleepStage
import com.samsung.healthcare.platform.domain.project.healthdata.Steps
import com.samsung.healthcare.platform.domain.project.healthdata.Weight
import org.springframework.data.annotation.Id

abstract class HealthDataEntity(
    @Id
    open val id: Int? = null,
    open val userId: String,
)

fun HealthData.toEntity(userId: UserId): HealthDataEntity =
    when (this.type) {
        BLOOD_PRESSURE -> (this as BloodPressure).toEntity(userId)
        HEART_RATE -> (this as HeartRate).toEntity(userId)
        SLEEP_SESSION -> (this as SleepSession).toEntity(userId)
        SLEEP_STAGE -> (this as SleepStage).toEntity(userId)
        STEPS -> (this as Steps).toEntity(userId)
        OXYGEN_SATURATION -> (this as OxygenSaturation).toEntity(userId)
        WEIGHT -> (this as Weight).toEntity(userId)
    }
