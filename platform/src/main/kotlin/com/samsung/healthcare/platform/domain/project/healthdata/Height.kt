package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded height data.
 *
 * @property meters Recorded height in meters.
 * @see SampleData
 */
data class Height(
    override val id: HealthDataId?,
    override val time: Instant,
    val meters: Double,
) : SampleData(id, time, HealthDataType.HEIGHT) {
    companion object {
        fun newHeight(
            time: Instant,
            meters: Double,
        ): Height = Height(null, time, meters)
    }
}
