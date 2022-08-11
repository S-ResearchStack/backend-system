package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.application.port.output.LoadIntervalDataCommand
import com.samsung.healthcare.platform.application.port.output.LoadIntervalDataPort
import com.samsung.healthcare.platform.domain.healthdata.HealthData
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Component

@Component
class IntervalDataStorageAdapter(
    private val repositoryLookup: HealthDataRepositoryLookup
) : LoadIntervalDataPort {

    override suspend fun findByPeriod(command: LoadIntervalDataCommand): Flow<HealthData> {
        TODO("Not yet implemented")
    }
}
