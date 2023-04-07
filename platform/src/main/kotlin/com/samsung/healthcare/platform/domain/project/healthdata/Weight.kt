package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded weight data.
 *
 * @property kilograms Recorded weight in Kilograms.
 * @see SampleData
 */
data class Weight(
    override val id: HealthDataId?,
    override val time: Instant,
    val kilograms: Double,
) : SampleData(id, time, HealthDataType.WEIGHT) {
    companion object {
        fun newWeight(
            time: Instant,
            kilograms: Double,
        ): Weight = Weight(null, time, kilograms)
    }
}
