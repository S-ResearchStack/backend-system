package com.samsung.healthcare.platform.domain.healthdata

import com.fasterxml.jackson.annotation.JsonValue

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

    enum class HealthDataType(
        @JsonValue val type: String,
        val isSampleData: Boolean = true,
        val dataClass: Class<out HealthData>
    ) {

        HEART_RATE("HeartRate", true, HeartRate::class.java),
        SLEEP_SESSION("SleepSession", false, SleepSession::class.java),
        SLEEP_STAGE("SleepStage", false, SleepStage::class.java),
        STEPS("Steps", false, Steps::class.java);

        companion object {
            private val stringToType = values().associateBy(HealthDataType::type)

            fun fromString(type: String) = stringToType[type]
        }
    }
}
