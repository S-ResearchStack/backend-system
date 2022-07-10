package com.samsung.healthcare.platform.application.port.input

data class CreateUserCommand(
    val userId: String,
    val profile: Map<String, Any> = emptyMap(),
)
