package com.samsung.healthcare.platform.domain.healthdata

import java.time.Instant

data class SleepSession(
    override val id: HealthDataId?,
    override val startTime: Instant,
    override val endTime: Instant,
    val title: String? = null,
    val notes: String? = null,
) : IntervalData(id, startTime, endTime, HealthDataType.SLEEP_SESSION) {
    companion object {
        fun newSleepSession(
            startTime: Instant,
            endTime: Instant,
            title: String? = null,
            notes: String? = null,
        ): SleepSession = SleepSession(null, startTime, endTime, title, notes)
    }
}
