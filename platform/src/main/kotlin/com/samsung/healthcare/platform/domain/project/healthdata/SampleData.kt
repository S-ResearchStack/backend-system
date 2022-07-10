package com.samsung.healthcare.platform.domain.project.healthdata

import java.time.Instant

/**
 * The SampleData class supports HealthData associated with a single [Instant].
 *
 * Currently supported data types include [HeartRate] and [BloodPressure].
 *
 * @property time Time of recording.
 * @see HealthData
 */
abstract class SampleData(
    id: HealthDataId?,
    open val time: Instant,
    type: HealthDataType,
) : HealthData(id, type)
