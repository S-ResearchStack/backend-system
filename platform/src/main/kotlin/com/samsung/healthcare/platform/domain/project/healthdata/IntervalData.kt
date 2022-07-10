package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * The IntervalData class supports HealthData associated with a given timeframe of occurrence.
 *
 * Currently supported data types include [Steps], [SleepStage], and [SleepSession].
 *
 * @property startTime Start time of the recording interval.
 * @property endTime End time of the recording interval.
 * @see HealthData
 */
abstract class IntervalData(
    id: HealthDataId?,
    open val startTime: Instant,
    open val endTime: Instant,
    type: HealthDataType,
) : HealthData(id, type)
