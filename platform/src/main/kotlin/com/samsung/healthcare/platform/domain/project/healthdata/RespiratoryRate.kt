package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded weight data.
 *
 * @property rpm Respiratory rate per minute.
 * @see SampleData
 */
data class RespiratoryRate(
    override val id: HealthDataId?,
    override val time: Instant,
    val rpm: Double,
) : SampleData(id, time, HealthDataType.RESPIRATORY_RATE) {
    companion object {
        fun newRespiratoryRate(
            time: Instant,
            rpm: Double,
        ): RespiratoryRate = RespiratoryRate(null, time, rpm)
    }
}
