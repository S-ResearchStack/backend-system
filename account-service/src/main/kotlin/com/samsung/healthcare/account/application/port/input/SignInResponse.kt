package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Account

data class SignInResponse(
    val account: Account,
    val jwt: String,
    val refreshToken: String
)
