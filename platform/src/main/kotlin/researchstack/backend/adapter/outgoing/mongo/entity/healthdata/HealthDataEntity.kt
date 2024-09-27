package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import researchstack.backend.enums.HealthDataType

@JsonIgnoreProperties(ignoreUnknown = true)
sealed interface HealthDataEntity {
    val id: String?
    val subjectId: String
}

fun HealthDataType.toHealthDataEntityClass() =
    when (this) {
        HealthDataType.ACCELEROMETER -> AccelerometerEntity::class.java
        HealthDataType.BATTERY -> BatteryEntity::class.java
        HealthDataType.BLOOD_GLUCOSE -> BloodGlucoseEntity::class.java
        HealthDataType.BLOOD_PRESSURE -> BloodPressureEntity::class.java
        HealthDataType.DEVICE_STAT_MOBILE_WEAR_CONNECTION -> DeviceStatMobileWearConnectionEntity::class.java
        HealthDataType.DEVICE_STAT_WEAR_BATTERY -> DeviceStatWearBatteryEntity::class.java
        HealthDataType.DEVICE_STAT_WEAR_OFF_BODY -> DeviceStatWearOffBodyEntity::class.java
        HealthDataType.DEVICE_STAT_WEAR_POWER_ON_OFF -> DeviceStatWearPowerOnOffEntity::class.java
        HealthDataType.EXERCISE -> ExerciseEntity::class.java
        HealthDataType.GYROSCOPE -> GyroscopeEntity::class.java
        HealthDataType.HEART_RATE -> HeartRateEntity::class.java
        HealthDataType.HEIGHT -> HeightEntity::class.java
        HealthDataType.LIGHT -> LightEntity::class.java
        HealthDataType.OFF_BODY -> OffBodyEntity::class.java
        HealthDataType.OXYGEN_SATURATION -> OxygenSaturationEntity::class.java
        HealthDataType.RESPIRATORY_RATE -> RespiratoryRateEntity::class.java
        HealthDataType.SLEEP_SESSION -> SleepSessionEntity::class.java
        HealthDataType.SLEEP_STAGE -> SleepStageEntity::class.java
        HealthDataType.STEPS -> StepsEntity::class.java
        HealthDataType.TOTAL_CALORIES_BURNED -> TotalCaloriesBurnedEntity::class.java
        HealthDataType.WEAR_ACCELEROMETER -> WearAccelerometerEntity::class.java
        HealthDataType.WEAR_BATTERY -> WearBatteryEntity::class.java
        HealthDataType.WEAR_BIA -> WearBiaEntity::class.java
        HealthDataType.WEAR_BLOOD_PRESSURE -> WearBloodPressureEntity::class.java
        HealthDataType.WEAR_ECG -> WearEcgEntity::class.java
        HealthDataType.WEAR_GYROSCOPE -> WearGyroscopeEntity::class.java
        HealthDataType.WEAR_HEALTH_EVENT -> WearHealthEventEntity::class.java
        HealthDataType.WEAR_HEART_RATE -> WearHeartRateEntity::class.java
        HealthDataType.WEAR_PPG_GREEN -> WearPpgGreenEntity::class.java
        HealthDataType.WEAR_PPG_IR -> WearPpgIrEntity::class.java
        HealthDataType.WEAR_PPG_RED -> WearPpgRedEntity::class.java
        HealthDataType.WEAR_SPO2 -> WearSpo2Entity::class.java
        HealthDataType.WEAR_SWEAT_LOSS -> WearSweatLossEntity::class.java
        HealthDataType.WEIGHT -> WeightEntity::class.java
        else -> throw IllegalArgumentException("Unsupported data type: $this")
    }
