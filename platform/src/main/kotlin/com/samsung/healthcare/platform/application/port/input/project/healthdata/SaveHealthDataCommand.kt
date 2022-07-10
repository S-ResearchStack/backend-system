package com.samsung.healthcare.platform.application.port.input.project.healthdata

import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType

data class SaveHealthDataCommand(
    val type: HealthDataType,
    val data: List<Map<String, Any>>
)
