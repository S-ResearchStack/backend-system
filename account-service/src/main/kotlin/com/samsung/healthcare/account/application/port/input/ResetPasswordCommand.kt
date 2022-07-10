package com.samsung.healthcare.account.application.port.input

data class ResetPasswordCommand(
    val resetToken: String,
    val password: String
) {
    init {
        require(resetToken.isNotBlank())
        require(password.isNotBlank())
    }
}
