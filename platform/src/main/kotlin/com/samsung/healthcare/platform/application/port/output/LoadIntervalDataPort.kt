package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.healthdata.HealthData
import kotlinx.coroutines.flow.Flow

interface LoadIntervalDataPort {
    suspend fun findByPeriod(command: LoadIntervalDataCommand): Flow<HealthData>
}
