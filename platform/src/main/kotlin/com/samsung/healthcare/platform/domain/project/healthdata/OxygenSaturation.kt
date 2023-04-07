package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded SP02 data.
 *
 * @property value Recorded value
 * @see SampleData
 */
data class OxygenSaturation(
    override val id: HealthDataId?,
    override val time: Instant,
    val value: Double,
) : SampleData(id, time, HealthDataType.OXYGEN_SATURATION) {
    companion object {
        fun newOxygenSaturation(time: Instant, value: Double): OxygenSaturation =
            OxygenSaturation(null, time, value)
    }
}
