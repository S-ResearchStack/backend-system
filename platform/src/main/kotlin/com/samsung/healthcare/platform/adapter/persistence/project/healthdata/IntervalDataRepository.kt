package com.samsung.healthcare.platform.adapter.persistence.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.HealthDataEntity
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface IntervalDataRepository<T : HealthDataEntity> : HealthDataRepository<T>
