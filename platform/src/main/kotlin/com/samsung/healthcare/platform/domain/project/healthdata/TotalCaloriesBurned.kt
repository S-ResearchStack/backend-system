package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * Represents recorded total calories burned data.
 *
 * @property calories The calories burned.
 * @see IntervalData
 */
data class TotalCaloriesBurned(
    override val id: HealthDataId?,
    override val startTime: Instant,
    override val endTime: Instant,
    val calories: Double,
) : IntervalData(id, startTime, endTime, HealthDataType.TOTAL_CALORIES_BURNED) {
    companion object {
        fun newTotalCaloriesBurned(
            startTime: Instant,
            endTime: Instant,
            calories: Double,
        ): TotalCaloriesBurned = TotalCaloriesBurned(null, startTime, endTime, calories)
    }
}
