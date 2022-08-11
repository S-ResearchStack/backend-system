package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.HealthData.HealthDataType
import java.time.LocalDateTime

class LoadSampleDataCommand(
    val type: HealthDataType,
    val users: List<UserId>,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)
