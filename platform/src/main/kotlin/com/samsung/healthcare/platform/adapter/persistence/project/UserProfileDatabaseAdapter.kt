package com.samsung.healthcare.platform.adapter.persistence.project

import com.samsung.healthcare.platform.adapter.persistence.entity.project.toEntity
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.exception.UserAlreadyExistsException
import com.samsung.healthcare.platform.application.port.output.project.UserProfileOutputPort
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserProfileDatabaseAdapter(
    private val repository: UserProfileRepository
) : UserProfileOutputPort {
    override suspend fun create(userProfile: UserProfile) {
        try {
            repository.save(userProfile.toEntity().also { it.setNew() })
        } catch (_: DataIntegrityViolationException) {
            throw UserAlreadyExistsException()
        }
    }

    override suspend fun updateLastSyncedAt(userId: UserProfile.UserId) {
        (
            repository.findById(userId.value)
                ?: throw ForbiddenException("This user(${userId.value}) is not registered on this project")
            )
            .copy(lastSyncedAt = LocalDateTime.now())
            .let { repository.save(it) }
    }

    override suspend fun existsByUserId(userId: UserProfile.UserId) =
        repository.existsById(userId.value)
}
