package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.application.port.input.CreateUserCommand

interface UserProfileInputPort {
    suspend fun registerUser(command: CreateUserCommand)
}
