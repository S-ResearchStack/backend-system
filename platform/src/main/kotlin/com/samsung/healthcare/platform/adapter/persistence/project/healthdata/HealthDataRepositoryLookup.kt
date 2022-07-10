package com.samsung.healthcare.platform.adapter.persistence.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.HealthDataEntity
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType
import org.springframework.stereotype.Component

// TODO naming
@Component
class HealthDataRepositoryLookup(
    bloodPressureRepository: BloodPressureRepository,
    heartRateRepository: HeartRateRepository,
    stepsRepository: StepsRepository,
    sleepSessionRepository: SleepSessionRepository,
    sleepStageRepository: SleepStageRepository,
) {
    @Suppress("UNCHECKED_CAST")
    private val typeToRepository: Map<HealthDataType, HealthDataRepository<HealthDataEntity>> =
        mapOf(
            HealthDataType.BLOOD_PRESSURE to (bloodPressureRepository as HealthDataRepository<HealthDataEntity>),
            HealthDataType.HEART_RATE to (heartRateRepository as HealthDataRepository<HealthDataEntity>),
            HealthDataType.STEPS to (stepsRepository as HealthDataRepository<HealthDataEntity>),
            HealthDataType.SLEEP_SESSION to (sleepSessionRepository as HealthDataRepository<HealthDataEntity>),
            HealthDataType.SLEEP_STAGE to (sleepStageRepository as HealthDataRepository<HealthDataEntity>),
        )

    fun getRepository(type: HealthDataType): HealthDataRepository<HealthDataEntity>? =
        typeToRepository[type]

    // TODO
}
