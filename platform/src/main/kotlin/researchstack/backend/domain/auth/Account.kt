package researchstack.backend.domain.auth

import researchstack.backend.domain.common.Email

data class Account(
    val id: String,
    val email: Email
)
