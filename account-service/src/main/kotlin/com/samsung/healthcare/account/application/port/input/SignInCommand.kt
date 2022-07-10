package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Email

data class SignInCommand(
    val email: Email,
    val password: String
) {
    init {
        require(password.isNotBlank())
    }
}
