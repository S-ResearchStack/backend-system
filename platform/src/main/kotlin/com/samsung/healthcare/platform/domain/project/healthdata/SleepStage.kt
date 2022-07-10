package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded sleep stage data.
 *
 * @property stage String value indicating which stage is associated with this SleepStage instance.
 * @see IntervalData
 */
data class SleepStage(
    override val id: HealthDataId?,
    override val startTime: Instant,
    override val endTime: Instant,
    val stage: String,
) : IntervalData(id, startTime, endTime, HealthDataType.SLEEP_STAGE) {
    companion object {
        fun newSleepStage(
            startTime: Instant,
            endTime: Instant,
            stage: String,
        ): SleepStage = SleepStage(null, startTime, endTime, stage)
    }
}
