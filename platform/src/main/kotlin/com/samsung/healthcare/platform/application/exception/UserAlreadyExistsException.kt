package com.samsung.healthcare.platform.application.exception

class UserAlreadyExistsException(override val message: String = "already registered user") : RuntimeException(message)
