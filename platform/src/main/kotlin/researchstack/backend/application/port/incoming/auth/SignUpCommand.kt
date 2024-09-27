package researchstack.backend.application.port.incoming.auth

data class SignUpCommand(
    val email: String,
    val password: String
) {
    init {
        require(password.isNotBlank())
    }
}
