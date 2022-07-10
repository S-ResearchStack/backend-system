package com.samsung.healthcare.platform.application.exception

class BadRequestException(override val message: String = "bad request") : RuntimeException(message)
