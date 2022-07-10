package com.samsung.healthcare.platform.adapter.persistence.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.HealthDataEntity
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

@NoRepositoryBean
interface HealthDataRepository<T : HealthDataEntity> : CoroutineCrudRepository<T, Int>
