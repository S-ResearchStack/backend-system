package com.samsung.healthcare.platform.adapter.persistence.project.task

import com.samsung.healthcare.platform.adapter.persistence.entity.project.task.ItemResultEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ItemResultRepository : CoroutineCrudRepository<ItemResultEntity, Int>
