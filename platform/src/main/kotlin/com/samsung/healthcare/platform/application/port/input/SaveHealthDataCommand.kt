package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.healthdata.HealthData.HealthDataType

data class SaveHealthDataCommand(
    val type: HealthDataType,
    val data: List<Map<String, Any>>
)
