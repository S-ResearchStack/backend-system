package researchstack.backend.adapter.outgoing.mongo.repository.healthdata

import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.entity.healthdata.HealthDataEntity
import researchstack.backend.enums.HealthDataType

@Component
class HealthDataRepositoryLookup(
    accelerometerRepository: AccelerometerRepository,
    batteryRepository: BatteryRepository,
    bloodGlucoseRepository: BloodGlucoseRepository,
    bloodPressureRepository: BloodPressureRepository,
    deviceStatMobileWearConnectionRepository: DeviceStatMobileWearConnectionRepository,
    deviceStatWearBatteryRepository: DeviceStatWearBatteryRepository,
    deviceStatWearOffBodyRepository: DeviceStatWearOffBodyRepository,
    deviceStatWearPowerOnOffRepository: DeviceStatWearPowerOnOffRepository,
    exerciseRepository: ExerciseRepository,
    gyroscopeRepository: GyroscopeRepository,
    heartRateRepository: HeartRateRepository,
    heightRepository: HeightRepository,
    lightRepository: ExerciseRepository,
    offBodyRepository: OffBodyRepository,
    oxygenSaturationRepository: OxygenSaturationRepository,
    respiratoryRateRepository: RespiratoryRateRepository,
    sleepSessionRepository: SleepSessionRepository,
    sleepStageRepository: SleepStageRepository,
    stepsRepository: StepsRepository,
    totalCaloriesBurnedRepository: TotalCaloriesBurnedRepository,
    wearAccelerometerRepository: WearAccelerometerRepository,
    wearBatteryRepository: WearBatteryRepository,
    wearBiaRepository: WearBiaRepository,
    wearBloodPressureRepository: WearBloodPressureRepository,
    wearEcgRepository: WearEcgRepository,
    wearGyroscopeRepository: WearGyroscopeRepository,
    wearHealthEventRepository: WearHealthEventRepository,
    wearHeartRateRepository: WearHeartRateRepository,
    wearPpgGreenRepository: WearPpgGreenRepository,
    wearPpgIrRepository: WearPpgIrRepository,
    wearPpgRedRepository: WearPpgRedRepository,
    wearSpo2Repository: WearSpo2Repository,
    wearSweatLossRepository: WearSweatLossRepository,
    weightRepository: WeightRepository
) {
    @Suppress("UNCHECKED_CAST")
    private val typeToRepository: Map<HealthDataType, HealthDataRepository<HealthDataEntity>> =
        mapOf(
            HealthDataType.ACCELEROMETER to accelerometerRepository,
            HealthDataType.BATTERY to batteryRepository,
            HealthDataType.BLOOD_GLUCOSE to bloodGlucoseRepository,
            HealthDataType.BLOOD_PRESSURE to bloodPressureRepository,
            HealthDataType.DEVICE_STAT_MOBILE_WEAR_CONNECTION to deviceStatMobileWearConnectionRepository,
            HealthDataType.DEVICE_STAT_WEAR_BATTERY to deviceStatWearBatteryRepository,
            HealthDataType.DEVICE_STAT_WEAR_OFF_BODY to deviceStatWearOffBodyRepository,
            HealthDataType.DEVICE_STAT_WEAR_POWER_ON_OFF to deviceStatWearPowerOnOffRepository,
            HealthDataType.EXERCISE to exerciseRepository,
            HealthDataType.GYROSCOPE to gyroscopeRepository,
            HealthDataType.HEART_RATE to heartRateRepository,
            HealthDataType.HEIGHT to heightRepository,
            HealthDataType.LIGHT to lightRepository,
            HealthDataType.OFF_BODY to offBodyRepository,
            HealthDataType.OXYGEN_SATURATION to oxygenSaturationRepository,
            HealthDataType.RESPIRATORY_RATE to respiratoryRateRepository,
            HealthDataType.SLEEP_SESSION to sleepSessionRepository,
            HealthDataType.SLEEP_STAGE to sleepStageRepository,
            HealthDataType.STEPS to stepsRepository,
            HealthDataType.TOTAL_CALORIES_BURNED to totalCaloriesBurnedRepository,
            HealthDataType.WEAR_ACCELEROMETER to wearAccelerometerRepository,
            HealthDataType.WEAR_BATTERY to wearBatteryRepository,
            HealthDataType.WEAR_BIA to wearBiaRepository,
            HealthDataType.WEAR_BLOOD_PRESSURE to wearBloodPressureRepository,
            HealthDataType.WEAR_ECG to wearEcgRepository,
            HealthDataType.WEAR_GYROSCOPE to wearGyroscopeRepository,
            HealthDataType.WEAR_HEALTH_EVENT to wearHealthEventRepository,
            HealthDataType.WEAR_HEART_RATE to wearHeartRateRepository,
            HealthDataType.WEAR_PPG_GREEN to wearPpgGreenRepository,
            HealthDataType.WEAR_PPG_IR to wearPpgIrRepository,
            HealthDataType.WEAR_PPG_RED to wearPpgRedRepository,
            HealthDataType.WEAR_SPO2 to wearSpo2Repository,
            HealthDataType.WEAR_SWEAT_LOSS to wearSweatLossRepository,
            HealthDataType.WEIGHT to weightRepository
        ) as Map<HealthDataType, HealthDataRepository<HealthDataEntity>>

    fun getRepository(type: HealthDataType): HealthDataRepository<HealthDataEntity>? = typeToRepository[type]
}
