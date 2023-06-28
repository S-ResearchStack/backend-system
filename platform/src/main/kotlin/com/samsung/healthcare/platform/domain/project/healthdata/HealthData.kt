package com.samsung.healthcare.platform.domain.project.healthdata

import com.fasterxml.jackson.annotation.JsonValue
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataId
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType

/**
 * HealthData is the base class for health data types collected
 * within a project.
 *
 * @property id [HealthDataId]
 * @property type [HealthDataType]
 */
abstract class HealthData(
    open val id: HealthDataId?,
    val type: HealthDataType,
) {
    data class HealthDataId private constructor(val value: Int) {
        companion object {
            fun from(value: Int?): HealthDataId {
                requireNotNull(value)
                require(0 < value)
                return HealthDataId(value)
            }
        }

        override fun toString(): String =
            value.toString()
    }

    /**
     * Class defining the type of HealthData.
     *
     * Enum entries are used to define the currently supported data types to facilitate
     * expanded support for other data types.
     *
     * @property type String value specifying the type of HealthData in question.
     * @property isSampleData Boolean indicating whether the data is an instance of [SampleData].
     * @property dataClass Specific class associated with the HealthData in question.
     */
    enum class HealthDataType(
        @JsonValue val type: String,
        val isSampleData: Boolean = true,
        val dataClass: Class<out HealthData>
    ) {
        BLOOD_PRESSURE("BloodPressure", true, BloodPressure::class.java),

        HEART_RATE("HeartRate", true, HeartRate::class.java),

        SLEEP_SESSION("SleepSession", false, SleepSession::class.java),

        SLEEP_STAGE("SleepStage", false, SleepStage::class.java),

        STEPS("Steps", false, Steps::class.java),

        WEIGHT("Weight", true, Weight::class.java),

        OXYGEN_SATURATION("OxygenSaturation", true, OxygenSaturation::class.java),

        HEIGHT("Height", true, Height::class.java),

        RESPIRATORY_RATE("RespiratoryRate", true, RespiratoryRate::class.java),

        TOTAL_CALORIES_BURNED("TotalCaloriesBurned", false, TotalCaloriesBurned::class.java),

        BLOOD_GLUCOSE("BloodGlucose", true, BloodGlucose::class.java);

        companion object {
            private val stringToType = values().associateBy(HealthDataType::type)

            fun fromString(type: String) = stringToType[type]
        }
    }
}
