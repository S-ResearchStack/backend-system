package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.User
import com.samsung.healthcare.platform.domain.User.UserId
import kotlinx.coroutines.flow.Flow

interface UserOutputPort {
    suspend fun create(user: User): UserId
    suspend fun update(user: User): User
    fun findAll(): Flow<User>
    suspend fun findById(id: UserId): User?
    suspend fun existsById(id: UserId): Boolean
    suspend fun deleteById(id: UserId): Boolean
}
