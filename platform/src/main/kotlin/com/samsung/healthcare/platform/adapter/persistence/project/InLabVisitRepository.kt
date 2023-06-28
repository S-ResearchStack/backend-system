package com.samsung.healthcare.platform.adapter.persistence.project

import com.samsung.healthcare.platform.adapter.persistence.entity.project.InLabVisitEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface InLabVisitRepository : CoroutineCrudRepository<InLabVisitEntity, Int>
