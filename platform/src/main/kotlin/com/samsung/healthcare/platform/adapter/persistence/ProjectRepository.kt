package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.ProjectEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProjectRepository : CoroutineCrudRepository<ProjectEntity, Int>
