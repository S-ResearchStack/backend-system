package com.samsung.healthcare.platform.adapter.persistence.entity.project

import com.samsung.healthcare.platform.adapter.persistence.entity.common.BaseUserIdEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.data.relational.core.mapping.Table

@Table("user_profiles")
class UserProfileEntity(
    val userId: String,
    val profile: Map<String, Any>,
) : BaseUserIdEntity<String>(userId) {
    companion object {
        fun fromDomain(userProfile: UserProfile): UserProfileEntity =
            UserProfileEntity(userProfile.userId, userProfile.profile)
    }

    fun toDomain(): UserProfile =
        UserProfile(this.userId, this.profile)
}
