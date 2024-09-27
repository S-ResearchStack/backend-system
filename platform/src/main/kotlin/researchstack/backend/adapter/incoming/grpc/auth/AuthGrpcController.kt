package researchstack.backend.adapter.incoming.grpc.auth

import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.auth.SignInCommand
import researchstack.backend.application.port.incoming.auth.SignInUseCase
import researchstack.backend.application.port.incoming.auth.SignUpCommand
import researchstack.backend.application.port.incoming.auth.SignUpUseCase
import researchstack.backend.grpc.AuthServiceGrpcKt
import researchstack.backend.grpc.SignInRequest
import researchstack.backend.grpc.SignInResponse
import researchstack.backend.grpc.SignUpRequest
import researchstack.backend.grpc.SignUpResponse

@Component
class AuthGrpcController(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : AuthServiceGrpcKt.AuthServiceCoroutineImplBase() {
    override suspend fun signIn(request: SignInRequest): SignInResponse {
        val email = request.email
        val password = request.password
        require(email.isNotEmpty()) { ExceptionMessage.EMPTY_EMAIL }
        require(password.isNotEmpty()) { ExceptionMessage.EMPTY_PASSWORD }

        return signInUseCase.signIn(
            SignInCommand(email, password)
        ).let {
            SignInResponse.newBuilder()
                .setId(it.id)
                .setEmail(it.email.value)
                .setAccessToken(it.accessToken)
                .setRefreshToken(it.refreshToken)
                .build()
        }
    }

    override suspend fun signUp(request: SignUpRequest): SignUpResponse {
        val email = request.email
        val password = request.password
        require(email.isNotEmpty()) { ExceptionMessage.EMPTY_EMAIL }
        require(password.isNotEmpty()) { ExceptionMessage.EMPTY_PASSWORD }

        return signUpUseCase.signUp(
            SignUpCommand(email, password)
        ).let {
            SignUpResponse.newBuilder()
                .setId(it.id)
                .setEmail(it.email.value)
                .build()
        }
    }
}
