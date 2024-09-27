package researchstack.backend.application.port.incoming.auth

interface SignUpUseCase {
    suspend fun signUp(command: SignUpCommand): SignUpResponse
}
