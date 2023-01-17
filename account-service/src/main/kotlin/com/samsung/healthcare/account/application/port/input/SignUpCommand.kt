package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Email

data class SignUpCommand(
    val email: Email,
    val password: String,
    val profile: Map<String, Any>,
) {
    init {
        require(password.isNotBlank())
    }
}
