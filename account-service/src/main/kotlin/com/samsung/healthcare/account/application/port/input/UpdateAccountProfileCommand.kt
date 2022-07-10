package com.samsung.healthcare.account.application.port.input

data class UpdateAccountProfileCommand(
    val accountId: String,
    val profile: Map<String, Any>
)
