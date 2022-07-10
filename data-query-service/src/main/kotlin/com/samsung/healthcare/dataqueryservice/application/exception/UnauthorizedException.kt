package com.samsung.healthcare.dataqueryservice.application.exception

class UnauthorizedException(override val message: String = "unauthorized") : RuntimeException(message)
