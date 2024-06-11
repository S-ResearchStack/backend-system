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
    weightRepository: WeightRepository,
    spO2Repository: OxygenSaturationRepository,
    totalCaloriesBurnedRepository: TotalCaloriesBurnedRepository,
    bloodGlucoseRepository: BloodGlucoseRepository,
    respiratoryRateRepository: RespiratoryRateRepository,
    heightRepository: HeightRepository,
    ecgRepository: EcgRepository,
) {
    @Suppress("UNCHECKED_CAST")
    private val typeToRepository: Map<HealthDataType, HealthDataRepository<HealthDataEntity>> =
        mapOf(
            HealthDataType.BLOOD_PRESSURE to bloodPressureRepository,
            HealthDataType.HEART_RATE to heartRateRepository,
            HealthDataType.STEPS to stepsRepository,
            HealthDataType.SLEEP_SESSION to sleepSessionRepository,
            HealthDataType.SLEEP_STAGE to sleepStageRepository,
            HealthDataType.WEIGHT to weightRepository,
            HealthDataType.OXYGEN_SATURATION to spO2Repository,
            HealthDataType.TOTAL_CALORIES_BURNED to totalCaloriesBurnedRepository,
            HealthDataType.BLOOD_GLUCOSE to bloodGlucoseRepository,
            HealthDataType.RESPIRATORY_RATE to respiratoryRateRepository,
            HealthDataType.HEIGHT to heightRepository,
            HealthDataType.ECG to ecgRepository,
        ) as Map<HealthDataType, HealthDataRepository<HealthDataEntity>>

    fun getRepository(type: HealthDataType): HealthDataRepository<HealthDataEntity>? =
        typeToRepository[type]
}
