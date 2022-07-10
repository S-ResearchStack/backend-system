package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.TaskResultEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskResultRepository : CoroutineCrudRepository<TaskResultEntity, Int>
