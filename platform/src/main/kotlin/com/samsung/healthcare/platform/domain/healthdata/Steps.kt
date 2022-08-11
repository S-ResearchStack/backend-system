package com.samsung.healthcare.platform.domain.healthdata

import java.time.Instant

data class Steps(
    override val id: HealthDataId?,
    override val startTime: Instant,
    override val endTime: Instant,
    val count: Long,
) : IntervalData(id, startTime, endTime, HealthDataType.STEPS) {
    companion object {
        fun newSteps(
            startTime: Instant,
            endTime: Instant,
            steps: Long,
        ): Steps = Steps(null, startTime, endTime, steps)
    }
}
