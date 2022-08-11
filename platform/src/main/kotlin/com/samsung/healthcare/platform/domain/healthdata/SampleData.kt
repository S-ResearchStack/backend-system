package com.samsung.healthcare.platform.domain.healthdata

import java.time.Instant

abstract class SampleData(
    id: HealthDataId?,
    open val time: Instant,
    type: HealthDataType,
) : HealthData(id, type)
