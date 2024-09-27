package researchstack.backend.application.port.incoming.auth

import researchstack.backend.domain.common.Email

data class SignUpResponse(
    val id: String,
    val email: Email
)
