package com.samsung.healthcare.platform.domain.healthdata

import java.time.Instant

data class HeartRate(
    override val id: HealthDataId?,
    override val time: Instant,
    val bpm: Long,
) : SampleData(id, time, HealthDataType.HEART_RATE) {
    companion object {
        fun newHeartRate(time: Instant, bpm: Long): HeartRate =
            HeartRate(null, time, bpm)
    }
}
