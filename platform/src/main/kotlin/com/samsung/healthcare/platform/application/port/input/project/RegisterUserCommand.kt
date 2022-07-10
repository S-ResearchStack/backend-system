package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.UserProfile.UserId

data class RegisterUserCommand(
    val id: UserId,
    val sub: String,
    val provider: String,
)
// TODO validate
