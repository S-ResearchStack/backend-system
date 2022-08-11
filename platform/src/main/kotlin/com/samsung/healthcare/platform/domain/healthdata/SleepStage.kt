package com.samsung.healthcare.platform.domain.healthdata

import java.time.Instant

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
