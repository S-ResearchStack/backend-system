package com.samsung.healthcare.platform.adapter.persistence.entity.project

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.UserProfileMapper
import com.samsung.healthcare.platform.adapter.persistence.entity.common.BaseUserIdEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_profiles")
data class UserProfileEntity(
    val userId: String,
    val profile: Map<String, Any>,
    val lastSyncedAt: LocalDateTime,
) : BaseUserIdEntity<String>(userId) {
    fun toDomain(): UserProfile =
        UserProfileMapper.INSTANCE.toDomain(this)
}

fun UserProfile.toEntity(): UserProfileEntity =
    UserProfileMapper.INSTANCE.toEntity(this)
