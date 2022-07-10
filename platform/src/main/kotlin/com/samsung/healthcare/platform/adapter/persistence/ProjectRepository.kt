package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProjectRepository : CoroutineCrudRepository<ProjectEntity, Int> {
    fun findByIdIn(idList: List<Int>): Flow<ProjectEntity>
}
