package com.samsung.healthcare.account.application.port.input

data class TokenRefreshCommand(
    val jwt: String,
    val refreshToken: String
) {
    init {
        require(jwt.isNotBlank())
        require(refreshToken.isNotBlank())
    }
}
