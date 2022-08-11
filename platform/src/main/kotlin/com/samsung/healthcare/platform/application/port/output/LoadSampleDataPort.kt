package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.healthdata.HealthData
import kotlinx.coroutines.flow.Flow

interface LoadSampleDataPort {
    suspend fun findByPeriod(command: LoadSampleDataCommand): Flow<HealthData>
}
