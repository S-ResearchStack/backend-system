package researchstack.backend.application.port.incoming.auth

interface SignInUseCase {
    suspend fun signIn(command: SignInCommand): SignInResponse
}
