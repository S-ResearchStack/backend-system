package com.samsung.healthcare.platform.application.exception

class InternalServerException(override val message: String = "internal server error") : RuntimeException(message)
