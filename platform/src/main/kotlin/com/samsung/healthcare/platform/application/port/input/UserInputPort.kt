package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.User
import com.samsung.healthcare.platform.domain.User.UserId
import kotlinx.coroutines.flow.Flow

interface UserInputPort {
    fun getUsers(): Flow<User>
    suspend fun findUserById(id: UserId): User
    suspend fun registerUser(command: RegisterUserCommand): UserId
    suspend fun deleteUserById(id: UserId)
    suspend fun existsById(id: UserId): Boolean
}
