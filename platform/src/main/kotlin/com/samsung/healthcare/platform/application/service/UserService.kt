package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.input.RegisterUserCommand
import com.samsung.healthcare.platform.application.port.input.UserInputPort
import com.samsung.healthcare.platform.application.port.output.UserOutputPort
import com.samsung.healthcare.platform.domain.User
import com.samsung.healthcare.platform.domain.User.UserId
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userOutputPort: UserOutputPort,
) : UserInputPort {
    override fun getUsers(): Flow<User> {
        return userOutputPort.findAll()
    }

    override suspend fun findUserById(id: UserId): User {
        return userOutputPort.findById(id) ?: throw NotFoundException("The user($id) does not exist.")
    }

    override suspend fun registerUser(command: RegisterUserCommand): UserId =
        userOutputPort.create(
            User.newUser(command.id, command.sub, command.provider)
        )

    override suspend fun deleteUserById(id: UserId) {
        if (!userOutputPort.deleteById(id)) throw NotFoundException("The user($id) does not exist.")
    }

    override suspend fun existsById(id: UserId): Boolean =
        userOutputPort.existsById(id)
}
