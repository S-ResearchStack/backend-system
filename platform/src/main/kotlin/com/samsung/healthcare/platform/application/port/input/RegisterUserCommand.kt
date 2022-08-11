package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.User.UserId

data class RegisterUserCommand(
    val id: UserId,
    val sub: String,
    val provider: String,
)
// TODO validate
