package com.samsung.healthcare.platform.application.exception

class NotFoundException(override val message: String = "not found") : RuntimeException(message)
