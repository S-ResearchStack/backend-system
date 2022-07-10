package com.samsung.healthcare.platform.adapter.persistence.project

import com.samsung.healthcare.platform.adapter.persistence.entity.project.toEntity
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.output.project.UserProfileOutputPort
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserProfileDatabaseAdapter(
    private val repository: UserProfileRepository
) : UserProfileOutputPort {
    override suspend fun create(userProfile: UserProfile) {
        repository.save(userProfile.toEntity().also { it.setNew() })
    }

    override suspend fun updateLastSyncedAt(userId: UserProfile.UserId) {
        (
            repository.findById(userId.value)
                ?: throw ForbiddenException("This user(${userId.value}) is not registered on this project")
            )
            .copy(lastSyncedAt = LocalDateTime.now(),)
            .let { repository.save(it) }
    }
}
