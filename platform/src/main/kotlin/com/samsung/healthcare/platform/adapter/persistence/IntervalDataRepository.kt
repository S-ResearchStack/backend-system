package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.healthdata.HealthDataEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.NoRepositoryBean
import java.time.LocalDateTime

@NoRepositoryBean
interface IntervalDataRepository<T : HealthDataEntity> : HealthDataRepository<T> {
    suspend fun findByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<T>
    suspend fun findByUserIdInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
        userId: Collection<String>,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Flow<T>
}
