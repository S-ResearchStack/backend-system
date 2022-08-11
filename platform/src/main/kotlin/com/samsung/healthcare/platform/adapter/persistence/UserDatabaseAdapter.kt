package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.UserEntity
import com.samsung.healthcare.platform.application.port.output.UserOutputPort
import com.samsung.healthcare.platform.domain.User
import com.samsung.healthcare.platform.domain.User.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserDatabaseAdapter(
    private val repository: UserRepository,
) : UserOutputPort {
    override suspend fun create(user: User): UserId =
        UserId.from(
            repository.save(UserEntity.fromDomain(user).also { it.setNew() })
                .id
        )

    override suspend fun update(user: User): User =
        repository.save(UserEntity.fromDomain(user)).toDomain()

    override fun findAll(): Flow<User> =
        repository.findAll().map { it.toDomain() }

    override suspend fun findById(id: UserId): User? =
        repository.findById(id.value)?.toDomain()

    override suspend fun existsById(id: UserId): Boolean =
        repository.existsById(id.value)

    override suspend fun deleteById(id: UserId): Boolean =
        repository.findById(id.value)?.let { user ->
            repository.save(user.copy(deletedAt = LocalDateTime.now()))
            true
        } ?: false
}
