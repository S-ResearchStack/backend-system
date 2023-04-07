package com.samsung.healthcare.platform.application.port.input

data class UpdateUserCommand(
    val profile: Map<String, Any> = emptyMap(),
)
