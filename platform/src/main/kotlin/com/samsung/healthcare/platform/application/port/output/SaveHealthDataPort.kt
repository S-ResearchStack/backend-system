package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.HealthData

interface SaveHealthDataPort {
    suspend fun save(userId: UserId, data: List<HealthData>)
}
