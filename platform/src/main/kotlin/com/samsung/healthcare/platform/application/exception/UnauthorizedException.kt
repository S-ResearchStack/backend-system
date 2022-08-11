package com.samsung.healthcare.platform.application.exception

class UnauthorizedException(override val message: String = "unauthorized") : RuntimeException(message)
