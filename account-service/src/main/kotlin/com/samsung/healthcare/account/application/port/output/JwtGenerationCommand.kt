package com.samsung.healthcare.account.application.port.output

import com.samsung.healthcare.account.domain.Role

data class JwtGenerationCommand(
    val issuer: String,
    val subject: String,
    val email: String,
    val roles: Collection<Role>,
    val lifeTime: Long
)
