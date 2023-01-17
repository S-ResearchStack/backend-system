package com.samsung.healthcare.account.application.exception

class InvalidEmailVerificationTokenException(
    message: String = "invalid email verification token"
) : RuntimeException(message)
