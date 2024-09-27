package researchstack.backend.application.port.incoming.auth

data class SignInCommand(
    val email: String,
    val password: String
)
