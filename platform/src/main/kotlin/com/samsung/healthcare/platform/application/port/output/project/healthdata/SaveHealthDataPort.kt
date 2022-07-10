package com.samsung.healthcare.platform.application.port.output.project.healthdata

import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType

interface SaveHealthDataPort {
    suspend fun save(userId: UserId, type: HealthDataType, data: List<HealthData>)
}
