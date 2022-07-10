package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded step count data.
 *
 * @property count The step count associated with this Steps instance.
 * @see IntervalData
 */
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
