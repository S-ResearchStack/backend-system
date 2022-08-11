package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.HealthData.HealthDataType
import java.time.LocalDateTime

data class GetHealthDataCommand(
    val types: List<HealthDataType>,
    val users: List<UserId>,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
)
