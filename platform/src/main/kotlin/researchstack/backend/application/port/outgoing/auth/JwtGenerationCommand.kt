package researchstack.backend.application.port.outgoing.auth

data class JwtGenerationCommand(
    val issuer: String,
    val subject: String,
    val email: String,
    val lifeTime: Long
)
