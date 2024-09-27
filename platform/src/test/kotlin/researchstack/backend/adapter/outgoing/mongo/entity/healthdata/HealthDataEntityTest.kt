package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.enums.HealthDataType

@ExperimentalCoroutinesApi
internal class HealthDataEntityTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `toHealthDataEntityClass should return the correct class for each HealthDataType enum value`() = runTest {
        assertEquals(AccelerometerEntity::class.java, HealthDataType.ACCELEROMETER.toHealthDataEntityClass())
        assertEquals(BatteryEntity::class.java, HealthDataType.BATTERY.toHealthDataEntityClass())
        assertEquals(BloodGlucoseEntity::class.java, HealthDataType.BLOOD_GLUCOSE.toHealthDataEntityClass())
        assertEquals(BloodPressureEntity::class.java, HealthDataType.BLOOD_PRESSURE.toHealthDataEntityClass())
        assertEquals(
            DeviceStatMobileWearConnectionEntity::class.java,
            HealthDataType.DEVICE_STAT_MOBILE_WEAR_CONNECTION.toHealthDataEntityClass()
        )
        assertEquals(
            DeviceStatWearBatteryEntity::class.java,
            HealthDataType.DEVICE_STAT_WEAR_BATTERY.toHealthDataEntityClass()
        )
        assertEquals(
            DeviceStatWearOffBodyEntity::class.java,
            HealthDataType.DEVICE_STAT_WEAR_OFF_BODY.toHealthDataEntityClass()
        )
        assertEquals(
            DeviceStatWearPowerOnOffEntity::class.java,
            HealthDataType.DEVICE_STAT_WEAR_POWER_ON_OFF.toHealthDataEntityClass()
        )
        assertEquals(ExerciseEntity::class.java, HealthDataType.EXERCISE.toHealthDataEntityClass())
        assertEquals(GyroscopeEntity::class.java, HealthDataType.GYROSCOPE.toHealthDataEntityClass())
        assertEquals(HeartRateEntity::class.java, HealthDataType.HEART_RATE.toHealthDataEntityClass())
        assertEquals(HeightEntity::class.java, HealthDataType.HEIGHT.toHealthDataEntityClass())
        assertEquals(LightEntity::class.java, HealthDataType.LIGHT.toHealthDataEntityClass())
        assertEquals(OffBodyEntity::class.java, HealthDataType.OFF_BODY.toHealthDataEntityClass())
        assertEquals(OxygenSaturationEntity::class.java, HealthDataType.OXYGEN_SATURATION.toHealthDataEntityClass())
        assertEquals(RespiratoryRateEntity::class.java, HealthDataType.RESPIRATORY_RATE.toHealthDataEntityClass())
        assertEquals(SleepSessionEntity::class.java, HealthDataType.SLEEP_SESSION.toHealthDataEntityClass())
        assertEquals(SleepStageEntity::class.java, HealthDataType.SLEEP_STAGE.toHealthDataEntityClass())
        assertEquals(StepsEntity::class.java, HealthDataType.STEPS.toHealthDataEntityClass())
        assertEquals(
            TotalCaloriesBurnedEntity::class.java,
            HealthDataType.TOTAL_CALORIES_BURNED.toHealthDataEntityClass()
        )
        assertEquals(WearAccelerometerEntity::class.java, HealthDataType.WEAR_ACCELEROMETER.toHealthDataEntityClass())
        assertEquals(WearBatteryEntity::class.java, HealthDataType.WEAR_BATTERY.toHealthDataEntityClass())
        assertEquals(WearBiaEntity::class.java, HealthDataType.WEAR_BIA.toHealthDataEntityClass())
        assertEquals(WearBloodPressureEntity::class.java, HealthDataType.WEAR_BLOOD_PRESSURE.toHealthDataEntityClass())
        assertEquals(WearEcgEntity::class.java, HealthDataType.WEAR_ECG.toHealthDataEntityClass())
        assertEquals(WearGyroscopeEntity::class.java, HealthDataType.WEAR_GYROSCOPE.toHealthDataEntityClass())
        assertEquals(WearHealthEventEntity::class.java, HealthDataType.WEAR_HEALTH_EVENT.toHealthDataEntityClass())
        assertEquals(WearHeartRateEntity::class.java, HealthDataType.WEAR_HEART_RATE.toHealthDataEntityClass())
        assertEquals(WearPpgGreenEntity::class.java, HealthDataType.WEAR_PPG_GREEN.toHealthDataEntityClass())
        assertEquals(WearPpgIrEntity::class.java, HealthDataType.WEAR_PPG_IR.toHealthDataEntityClass())
        assertEquals(WearPpgRedEntity::class.java, HealthDataType.WEAR_PPG_RED.toHealthDataEntityClass())
        assertEquals(WearSpo2Entity::class.java, HealthDataType.WEAR_SPO2.toHealthDataEntityClass())
        assertEquals(WearSweatLossEntity::class.java, HealthDataType.WEAR_SWEAT_LOSS.toHealthDataEntityClass())
        assertEquals(WeightEntity::class.java, HealthDataType.WEIGHT.toHealthDataEntityClass())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `toHealthDataEntityClass should throw an exception for unsupported HealthDataType enum value`() = runTest {
        assertThrows<IllegalArgumentException> { HealthDataType.valueOf("UNKNOWN").toHealthDataEntityClass() }
    }
}
