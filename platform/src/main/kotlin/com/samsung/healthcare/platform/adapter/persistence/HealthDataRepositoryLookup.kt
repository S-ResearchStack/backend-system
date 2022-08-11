package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.healthdata.HealthDataEntity
import com.samsung.healthcare.platform.domain.healthdata.HealthData.HealthDataType
import org.springframework.stereotype.Component

// TODO naming
@Component
class HealthDataRepositoryLookup(
    heartRateRepository: HeartRateRepository,
    stepsRepository: StepsRepository,
    sleepSessionRepository: SleepSessionRepository,
) {
    @Suppress("UNCHECKED_CAST")
    private val typeToRepository: Map<HealthDataType, HealthDataRepository<HealthDataEntity>> =
        mapOf(
            HealthDataType.HEART_RATE to (heartRateRepository as HealthDataRepository<HealthDataEntity>),
            HealthDataType.STEPS to (stepsRepository as HealthDataRepository<HealthDataEntity>),
            HealthDataType.SLEEP_SESSION to (sleepSessionRepository as HealthDataRepository<HealthDataEntity>),
        )

    fun getRepository(type: HealthDataType): HealthDataRepository<HealthDataEntity>? =
        typeToRepository[type]

    // TODO
}
