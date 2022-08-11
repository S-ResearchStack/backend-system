package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.project.UserProfileEntity
import com.samsung.healthcare.platform.application.port.output.UserProfileOutputPort
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.stereotype.Component

@Component
class UserProfileDatabaseAdapter(
    private val repository: UserProfileRepository
) : UserProfileOutputPort {
    override suspend fun create(userProfile: UserProfile) {
        repository.save(UserProfileEntity.fromDomain(userProfile).also { it.setNew() })
    }
}
