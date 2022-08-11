package com.samsung.healthcare.platform.adapter.persistence.entity.healthdata

import java.time.LocalDateTime

abstract class IntervalDataEntity(
    id: Int? = null,
    userId: String,
    open val startTime: LocalDateTime,
    open val endTime: LocalDateTime,
) : HealthDataEntity(id, userId)
