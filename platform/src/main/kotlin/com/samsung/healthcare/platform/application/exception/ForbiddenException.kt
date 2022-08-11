package com.samsung.healthcare.platform.application.exception

class ForbiddenException(override val message: String = "forbidden") : RuntimeException(message)
