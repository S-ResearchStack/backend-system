package com.samsung.healthcare.account.application.port.input

data class TokenRefreshResponse(
    val jwt: String,
    val refreshToken: String
)
