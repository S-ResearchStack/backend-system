package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.project.UserProfileEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserProfileRepository : CoroutineCrudRepository<UserProfileEntity, String>
