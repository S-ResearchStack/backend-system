package com.samsung.healthcare.account.application.exception

class UnauthorizedException(override val message: String = "unauthorized") : RuntimeException(message)
