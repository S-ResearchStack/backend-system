package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded heart rate data.
 *
 * @property bpm Recorded BPM.
 * @see SampleData
 */
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
