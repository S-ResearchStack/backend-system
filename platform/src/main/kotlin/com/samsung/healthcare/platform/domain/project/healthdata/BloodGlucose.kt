package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded weight data.
 *
 * @property millimolesPerLiter millimoles per liter.
 * @see SampleData
 */
data class BloodGlucose(
    override val id: HealthDataId?,
    override val time: Instant,
    val millimolesPerLiter: Double,
) : SampleData(id, time, HealthDataType.BLOOD_GLUCOSE) {
    companion object {
        fun newBloodGlucose(
            time: Instant,
            millimolesPerLiter: Double,
        ): BloodGlucose = BloodGlucose(null, time, millimolesPerLiter)
    }
}
