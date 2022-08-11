package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.healthdata.HealthDataEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.NoRepositoryBean
import java.time.LocalDateTime

@NoRepositoryBean
interface SampleDataRepository<T : HealthDataEntity> : HealthDataRepository<T> {
    suspend fun findByTimeBetween(startDate: LocalDateTime, endDate: LocalDateTime): Flow<T>
    suspend fun findByUserIdInAndTimeBetween(
        userId: Collection<String>,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Flow<T>
}
