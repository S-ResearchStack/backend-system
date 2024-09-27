package researchstack.backend.application.port.incoming.auth

import researchstack.backend.domain.common.Email

data class SignInResponse(
    val id: String,
    val email: Email,
    val accessToken: String,
    val refreshToken: String
)
